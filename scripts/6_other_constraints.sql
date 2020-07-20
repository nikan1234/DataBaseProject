CREATE OR REPLACE TRIGGER LOCOMOTIVE_TEAM_CHECK
BEFORE INSERT OR UPDATE ON LOCOMOTIVES
FOR EACH ROW
DECLARE
    drivers_count  INTEGER;
    repairman_count INTEGER;
BEGIN 
    IF :new.team_id IS NULL THEN
        RETURN;
    END IF;

    SELECT count(*)
    INTO drivers_count
    FROM DRIVER 
    WHERE team_id = :new.team_id;

    SELECT count(*)
    INTO repairman_count
    FROM REPAIRMAN 
    WHERE team_id = :new.team_id;

    IF repairman_count = 0 OR drivers_count = 0 THEN 
        raise_application_error(
                    -20219, 'Locomotive team must have driver and repairman');
    END IF;
END;
/


-- REPAIR
DROP SEQUENCE REPAIR_SEQUENCE;
CREATE SEQUENCE REPAIR_SEQUENCE minvalue 0;

CREATE OR REPLACE TRIGGER REPAIR_AUTOINCR
BEFORE INSERT OR UPDATE
   ON REPAIR
   FOR EACH ROW
DECLARE
    repairmen_count  INTEGER;
    n                INTEGER;
BEGIN
  -- Проверим, что у локомотива есть ремонтник
  SELECT COUNT(*)
  INTO repairmen_count 
  FROM LOCOMOTIVES INNER JOIN REPAIRMAN USING(team_id) 
  WHERE locomotive_id = :new.locomotive_id;
  
  If repairmen_count = 0 THEN 
    raise_application_error(-20223, 'Locomotive requires repairman');
  END IF;

  -- Проверим, что локомотив не находится в рейсе
  SELECT COUNT(*) INTO n
  FROM FLIGHTS_TIMETABLE 
  WHERE :new.repair_date BETWEEN TO_DATE(START_TIME, 'DD-MM-YYYY HH24:MI:SS')
                         AND     TO_DATE(ARRIVAL_TIME, 'DD-MM-YYYY HH24:MI:SS');
  IF n > 0 THEN
    raise_application_error(-20218, 'Has intersections with flights');
  END IF;
  

     :new.repair_id := REPAIR_SEQUENCE.NextVal;
END REPAIR_AUTOINCR;
/


-- Если сдвигаем рейс, то сдвигаем и проверку 
CREATE OR REPLACE TRIGGER DELAY_INSPECTION_CHECK_ADD 
AFTER INSERT ON DELAYS 
FOR EACH ROW 
BEGIN
    UPDATE INSPECTIONS 
    SET inspection_date = (SELECT TO_DATE(TO_CHAR(flight_date + :new.delay_duration / 24, 'YYYY-MM-DD') 
                                          || ' 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
                           FROM FLIGHTS WHERE flight_number = :new.flight_number)
    WHERE flight_number = :new.flight_number;
END;      
/


-- Если убираем сдвиг, то возвращаем время проверки назад
CREATE OR REPLACE TRIGGER DELAY_COLLISION_CHECK_SUB
AFTER DELETE ON DELAYS 
FOR EACH ROW 
BEGIN
    UPDATE INSPECTIONS 
    SET inspection_date = (SELECT TO_DATE(TO_CHAR(flight_date, 'YYYY-MM-DD') 
                                          || ' 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
                           FROM FLIGHTS WHERE flight_number = :old.flight_number)
    WHERE flight_number = :old.flight_number;
END;
/