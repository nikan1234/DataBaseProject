CREATE OR REPLACE PROCEDURE DELAY_CANCEL_CHECK (
    flight          IN INTEGER,
    operation_date  IN VARCHAR
) AS 
    flight_date DATE;
begin
    SELECT flight_date
    INTO flight_date   
    FROM FLIGHTS 
    WHERE flight_number = flight;

    if (TO_DATE(operation_date, 'DD-MM-YYYY HH24:MI:SS') > flight_date) then
        raise_application_error(-20224, 'Wrong operation time');
    end if;
end;
/
    

-- Добавить задержку
CREATE OR REPLACE PROCEDURE ADD_DELAY (
    flight_number   IN INTEGER,
    delay_cause     IN VARCHAR,
    delay_duration  IN INTEGER,
    operation_date  IN VARCHAR
) AS 
begin
    DELAY_CANCEL_CHECK(flight_number, operation_date);
    insert into DELAYS values (0, flight_number, delay_cause, delay_duration);
end;
/


-- Удалить задержку
CREATE OR REPLACE PROCEDURE REMOVE_DELAY (
    flight          IN INTEGER,
    operation_date  IN VARCHAR
) AS 
begin
    DELAY_CANCEL_CHECK(flight, operation_date);
    delete from DELAYS where flight_number = flight;
end;
/
    

-- Добавить отмену 
CREATE OR REPLACE PROCEDURE ADD_CANCELLATION (
    flight_number   IN INTEGER,
    cancel_cause     IN VARCHAR,
    operation_date  IN VARCHAR
) AS 
begin
    DELAY_CANCEL_CHECK(flight_number, operation_date);
    insert into CANCELLATIONS values (0, flight_number, cancel_cause);
end;
/


-- Удалить отмену
CREATE OR REPLACE PROCEDURE REMOVE_CANCELLATION (
    flight          IN INTEGER,
    operation_date  IN VARCHAR
) AS 
begin
    DELAY_CANCEL_CHECK(flight, operation_date);
    delete from CANCELLATIONS where flight_number = flight;
end;
/


CREATE OR REPLACE PROCEDURE TIMETABLE_CHECK_PROCEDURE (
    CUR_flight_number           IN INTEGER,
    CUR_flight_locomotive       IN INTEGER,
    CUR_flight_date             IN DATE,
    CUR_flight_duration         IN INTEGER
) AS
    SPACE INTEGER;
    n     INTEGER;
    delayed_flight_date DATE;
BEGIN 
    SPACE := 1;


    -- Проверим, не ремонтируется ли локомотив во время рейса
    SELECT COUNT(*) INTO n
    FROM REPAIR 
    WHERE repair_date BETWEEN CUR_flight_date 
                          AND CUR_flight_date + CUR_flight_duration / 24
    AND locomotive_id = CUR_flight_locomotive;

    IF n > 0 THEN 
        raise_application_error(-20226, 'Locomotive has repair');
    END IF;
                    
    
    -- Для не отмененных и не задержанных
        FOR v_gt IN (
            SELECT T.flight_number, T.duration, F.flight_date
            FROM FLIGHTS_TIMETABLE T INNER JOIN FLIGHTS F ON T.flight_number = F.flight_number
            WHERE T.locomotive_id = CUR_flight_locomotive
            AND T.has_delay = 'N' AND T.has_cancellation = 'N'
            AND T.flight_number <> CUR_flight_number
        )
        LOOP
            IF CUR_flight_date < v_gt.flight_date THEN
                IF CUR_flight_date + (CUR_flight_duration + SPACE) / 24 > v_gt.flight_date THEN
                    raise_application_error(
                        -20218, 'Has intersections with flight #' || v_gt.flight_number);
                END IF;
            ELSE 
                IF v_gt.flight_date + (v_gt.duration + SPACE) / 24 > CUR_flight_date THEN
                    raise_application_error(
                        -20218, 'Has intersections with flight #' || v_gt.flight_number);
                END IF;
            END IF;
        END LOOP;

    -- Для не отмененных и задержанных
        FOR v_gt IN (
            SELECT T.flight_number, T.duration, T.delay_duration, F.flight_date
            FROM FLIGHTS_TIMETABLE T INNER JOIN FLIGHTS F ON T.flight_number = F.flight_number
            WHERE T.locomotive_id = CUR_flight_locomotive
            AND T.has_delay = 'Y' AND T.has_cancellation = 'N'  
            AND T.flight_number <> CUR_flight_number  
        )
        LOOP 
            delayed_flight_date := v_gt.flight_date + v_gt.delay_duration / 24;
            IF CUR_flight_date < delayed_flight_date THEN
                IF CUR_flight_date + (CUR_flight_duration + SPACE) / 24 > delayed_flight_date THEN
                    raise_application_error(
                        -20218, 'Has intersections with flight #' || v_gt.flight_number);
                END IF;
            ELSE 
                IF delayed_flight_date + (v_gt.duration + SPACE) / 24 > CUR_flight_date THEN
                    raise_application_error(
                        -20218, 'Has intersections with flight #' || v_gt.flight_number);
                END IF;
            END IF;
        END LOOP;
END TIMETABLE_CHECK_PROCEDURE;
/

-- При добавлении рейса проверяем, что нет коллизии с другими рейсами этого локомотива
-- 1. Проверяем не отмененные не задержанные рейсы
-- 2. Проверяем не отмененные задержанные рейсы с учетом задержки
CREATE OR REPLACE TRIGGER TIMETABLE_COLLISION_CHECK 
BEFORE INSERT OR UPDATE
    ON FLIGHTS
FOR EACH ROW 
DECLARE
    delayed_flight_date DATE;
    new_flight_locomotive INTEGER;
    new_flight_date DATE;
    new_flight_duration INTEGER;
    SPACE           INTEGER;
    drivers_count   INTEGER;
BEGIN
  -- Между рейсам необходим промежуток в один час
  -- для возможности проведения подготовки к новому рейсу
  SPACE := 1;

  new_flight_locomotive := :new.locomotive_id;
  new_flight_date       := :new.flight_date;

  SELECT route_duration 
  INTO new_flight_duration
  FROM ROUTE_DURATIONS
  WHERE route_id = :new.route_id;

  --Также проверим, что у локомотива есть водитель
  SELECT COUNT(*)
  INTO drivers_count 
  FROM LOCOMOTIVES INNER JOIN DRIVER USING(team_id) 
  WHERE locomotive_id = new_flight_locomotive;

  if drivers_count = 0 THEN 
    raise_application_error(-20222, 'Locomotive requires driver');
  END IF;

  TIMETABLE_CHECK_PROCEDURE(
      :new.flight_number, 
      new_flight_locomotive, 
      new_flight_date, 
      new_flight_duration
   );
END;
/



CREATE OR REPLACE TRIGGER TIMETABLE_CLEANUP
AFTER DELETE ON FLIGHTS
BEGIN
    DELETE FROM DELAYS WHERE flight_number IS NULL;
    DELETE FROM CANCELLATIONS WHERE flight_number IS NULL;
END;
/


DROP SEQUENCE DELAY_SEQUENCE;
CREATE SEQUENCE DELAY_SEQUENCE minvalue 0;

-- При добавлении задрежки проверяем, на появятся ли коллизии
-- (Могут появиться, т.к. временной отрезок рейса сдвигается)
CREATE OR REPLACE TRIGGER DELAY_COLLISION_CHECK
BEFORE INSERT
    ON DELAYS
FOR EACH ROW
DECLARE
    delayed_flight_date DATE;
    delayed_flight_duration INTEGER;
    delayed_flight_locomotive INTEGER;
    tmp_delayed DATE;
    SPACE INTEGER;
BEGIN
    -- Между рейсам необходим промежуток в один час
    -- для возможности проведения подготовки к новому рейсу
    SPACE := 1;


    IF :new.flight_number IS NULL THEN
        raise_application_error(
                    -20219, 'Flight cant be null');
    END IF;


    SELECT F.flight_date, T.duration, T.locomotive_id
    INTO delayed_flight_date, delayed_flight_duration, delayed_flight_locomotive
    FROM FLIGHTS_TIMETABLE T INNER JOIN FLIGHTS F ON T.flight_number = F.flight_number
    WHERE T.flight_number = :new.flight_number;

    delayed_flight_date := delayed_flight_date + :new.delay_duration / 24;

    TIMETABLE_CHECK_PROCEDURE(
        :new.flight_number, 
        delayed_flight_locomotive, 
        delayed_flight_date, 
        delayed_flight_duration
    );
    :new.delay_id := DELAY_SEQUENCE.NextVal;
END;
/



-- При добавлении отмены ничего не проверяем
DROP SEQUENCE CANCEL_SEQUENCE;
CREATE SEQUENCE CANCEL_SEQUENCE minvalue 0;

CREATE OR REPLACE  TRIGGER CANCEL_AUTOINCR
BEFORE INSERT OR UPDATE
   ON CANCELLATIONS
   FOR EACH ROW
BEGIN    
    :new.cancel_id := CANCEL_SEQUENCE.NextVal;
END;
/



-- При удалении задержки проверяем что не возникнет коллизий
-- (Т.к. мог быть добавлен потенциально конфликтующий рейс)
CREATE OR REPLACE TRIGGER REMOVE_DELAY_COLLISION_CHECK
for delete on DELAYS compound trigger

    delayed_flight_date DATE;
    CUR_flight_number INTEGER;
    CUR_flight_locomotive INTEGER;
    CUR_flight_date DATE;
    CUR_flight_duration INTEGER;
    SPACE INTEGER;

before each row is 
  begin
    SPACE := 1;

        IF :old.flight_number IS NOT NULL THEN
            SELECT flight_date, route_duration, locomotive_id
            INTO CUR_flight_date, CUR_flight_duration, CUR_flight_locomotive
            FROM ROUTE_DURATIONS INNER JOIN FLIGHTS USING(route_id)
            WHERE flight_number = :old.flight_number;

            CUR_flight_number := :old.flight_number;
        END IF;
  end before each row;

  after statement is 
  begin
    TIMETABLE_CHECK_PROCEDURE(
        CUR_flight_number,
        CUR_flight_locomotive,
        CUR_flight_date,
        CUR_flight_duration
    );
  end after statement;
END;
/


-- При удалении отмены проверяем что не возникнет коллизий
-- (Т.к. мог быть добавлен потенциально конфликтующий рейс)
CREATE OR REPLACE TRIGGER REMOVE_CANCEL_COLLISION_CHECK 
for delete on CANCELLATIONS compound trigger

    delayed_flight_date DATE;
    CUR_flight_number INTEGER;
    CUR_flight_locomotive INTEGER;
    CUR_flight_date DATE;
    CUR_flight_duration INTEGER;
    SPACE INTEGER;

before each row is 
  begin
    SPACE := 1;

        IF :old.flight_number IS NOT NULL THEN
            SELECT flight_date, route_duration, locomotive_id
            INTO CUR_flight_date, CUR_flight_duration, CUR_flight_locomotive
            FROM ROUTE_DURATIONS INNER JOIN FLIGHTS USING(route_id)
            WHERE flight_number = :old.flight_number;

            CUR_flight_number := :old.flight_number;
        END IF;
  end before each row;

  after statement is 
  begin
    TIMETABLE_CHECK_PROCEDURE(
        CUR_flight_number,
        CUR_flight_locomotive,
        CUR_flight_date,
        CUR_flight_duration
    );
  end after statement;
END;
/​