--Добавить купленный билет
CREATE OR REPLACE PROCEDURE BUY_TICKET (
    passenger_last_name         IN VARCHAR,
    passenger_first_name        IN VARCHAR,
    passenger_second_name       IN VARCHAR,
    has_baggage                 IN CHAR,
    passenger_age               IN NUMBER,
    passenger_gender            IN CHAR,   
    flight                      IN INTEGER,
    operation_date              IN VARCHAR,
    ticket_id                   OUT VARCHAR
) AS
    n       INTEGER;
    max_id  INTEGER;
BEGIN
    --Создаем идентификатор для билета
    SELECT COUNT(*)
    INTO n FROM TICKETS T WHERE T.flight_number = flight;    
    IF n = 0 THEN
        ticket_id := TO_CHAR(flight) || '-' || TO_CHAR(1);    
    ELSE 
        SELECT max(TO_NUMBER(SUBSTR(ticket_id, INSTR(ticket_id, '-') + 1)))
        INTO max_id
        FROM TICKETS T 
        WHERE T.flight_number = flight;

        ticket_id := TO_CHAR(flight) || '-' || TO_CHAR(max_id + 1);    
    END IF;
   
    --Добавляем в таблицу
    INSERT INTO TICKETS VALUES (
        ticket_id, 
        passenger_last_name,
        passenger_first_name,
        passenger_second_name,
        has_baggage,
        passenger_age,
        passenger_gender,
        flight,
        'Y',
        TO_DATE(operation_date, 'YYYY-MM-DD HH24:MI:SS')
    );
END BUY_TICKET;
/


-- Добавить забронированный билет
CREATE OR REPLACE PROCEDURE RESERVE_TICKET(
    passenger_last_name         IN VARCHAR,
    passenger_first_name        IN VARCHAR,
    passenger_second_name       IN VARCHAR,
    has_baggage                 IN CHAR,
    passenger_age               IN NUMBER,
    passenger_gender            IN CHAR,   
    flight                      IN INTEGER,
    operation_date              IN VARCHAR,
    ticket_id                   OUT VARCHAR
) AS
   n        INTEGER;
   max_id   INTEGER;
BEGIN
    --Создаем идентификатор для билета
    SELECT COUNT(*)
    INTO n FROM TICKETS T WHERE T.flight_number = flight;    
    IF n = 0 THEN
        ticket_id := TO_CHAR(flight) || '-' || TO_CHAR(1);    
    ELSE 
        SELECT max(TO_NUMBER(SUBSTR(ticket_id, INSTR(ticket_id, '-') + 1)))
        INTO max_id
        FROM TICKETS T 
        WHERE T.flight_number = flight;

        ticket_id := TO_CHAR(flight) || '-' || TO_CHAR(max_id + 1);    
    END IF; 
   
    --Добавляем в таблицу
    INSERT INTO TICKETS VALUES (
        ticket_id, 
        passenger_last_name,
        passenger_first_name,
        passenger_second_name,
        has_baggage,
        passenger_age,
        passenger_gender,
        flight,
        'N',
        TO_DATE(operation_date, 'YYYY-MM-DD HH24:MI:SS')
    );
END RESERVE_TICKET;
/


CREATE OR REPLACE PROCEDURE OPERATON_DATE_CHECK_PROCEDURE (
    ticket              IN VARCHAR,
    operation_date      IN DATE,
    flight              IN INTEGER,
    flight_date_        IN DATE
) AS
    n            INTEGER;
    duration     INTEGER;
BEGIN
    --Проверяем, можем ли мы выполнить операцию
    --Можем, если делаем это до начала рейса (с учетом задержек)
    SELECT COUNT(*)
    INTO n FROM DELAYS D
    WHERE D.flight_number = flight;

    --Есть задержка, прибавляем ее ко времени отправления
    IF n = 1 THEN
        SELECT delay_duration
        INTO duration FROM DELAYS D
        WHERE D.flight_number = flight;

        IF (operation_date > flight_date_ + duration / 24) THEN
            raise_application_error
            (-20201, 'You cant return a ticket for this flight');
        END IF;
    END IF;

    --Задержки нет - рейс по расписанию
    IF n = 0 THEN 
        IF (operation_date > flight_date_ ) THEN
            raise_application_error
            (-20201, 'You cant return a ticket for this flight');
        END IF;
    END IF;
END OPERATON_DATE_CHECK_PROCEDURE;
/



--Возвращем билет
CREATE OR REPLACE PROCEDURE RETURN_TICKET (
    ticket                      IN VARCHAR,
    operation_date              IN VARCHAR
) AS 
    return_date     DATE;
    old_date        DATE;
    flight          INTEGER;
    flight_date     DATE;
    duration        INTEGER;
    n               INTEGER;
BEGIN
    return_date := TO_DATE(operation_date, 'YYYY-MM-DD HH24:MI:SS');

    --Проверяем, есть ли такой билет в базе
    SELECT COUNT(*)
    INTO n FROM TICKETS T
    WHERE T.ticket_id = ticket 
    AND T.purchased = 'Y';
    IF n <> 1 THEN
        raise_application_error
        (-20202, 'No tickets found');
    END IF;

    SELECT operation_date 
    INTO old_date 
    FROM TICKETS T
    WHERE T.ticket_id = ticket 
    AND T.purchased = 'Y';

    IF (old_date > return_date) THEN 
        raise_application_error(-20203, 'WRONG TIME');
    END IF;

    --Получаем номер рейса и дату
    SELECT flight_number, flight_date
    INTO flight, flight_date
    FROM TICKETS INNER JOIN FLIGHTS USING(flight_number)
    WHERE ticket_id = ticket;
 
    --Проверяем, что рейс отменен
    SELECT COUNT(*)
    INTO n FROM CANCELLATIONS
    WHERE flight_number = flight;
 
    IF n = 0 THEN 
        OPERATON_DATE_CHECK_PROCEDURE(ticket, return_date, flight, flight_date);
    END IF;
    
    DELETE FROM TICKETS T WHERE T.ticket_id = ticket;
END RETURN_TICKET;
/



--Оплачиваем забронированный билет
CREATE OR REPLACE PROCEDURE PURCHASE_TICKET (
    ticket          IN VARCHAR,
    purchase_date   IN VARCHAR
) AS
    n INTEGER;
BEGIN
    --Ищем неоплаченный билет с таким номером
    SELECT COUNT(*)
    INTO n
    FROM TICKETS T
    WHERE T.ticket_id = ticket
    AND T.purchased = 'N';

    --Если такого билета нет
    if n <> 1 THEN
        raise_application_error(-20202, 'No tickets found');
    END IF;

    --Обновляем, в том числе дату операции
    UPDATE TICKETS T
    SET T.purchased = 'Y',
        T.operation_date = TO_DATE(purchase_date, 'YYYY-MM-DD HH24:MI:SS')
    WHERE T.ticket_id = ticket;
    
END PURCHASE_TICKET;
/



-- Нельзя купить билет на отмененный рейс.
-- На рейс можно купить билет до отправиления
-- Если рейс задерживается - до то до времени отправления
-- плюс задержка  
CREATE OR REPLACE TRIGGER TICKET_DATE_CHECK 
BEFORE INSERT
ON TICKETS
FOR EACH ROW 
DECLARE 
    max_id          INTEGER;
    flight          INTEGER;
    flight_date     DATE;
    n INTEGER;
    w INTEGER;
BEGIN
    --Для вставки через INSERT чтобы задать правильный номер билета
    SELECT COUNT(*)
    INTO n FROM TICKETS T WHERE T.flight_number = :new.flight_number;    
    IF n = 0 THEN
        :new.ticket_id := TO_CHAR(:new.flight_number) || '-' || TO_CHAR(1);    
    ELSE 
        SELECT max(TO_NUMBER(SUBSTR(ticket_id, INSTR(ticket_id, '-') + 1)))
        INTO max_id
        FROM TICKETS T 
        WHERE T.flight_number = :new.flight_number;

        :new.ticket_id := TO_CHAR(:new.flight_number) || '-' || TO_CHAR(max_id + 1);    
    END IF;

    --Проверяем, что рейс не отменен
    SELECT COUNT(*)
    INTO n FROM CANCELLATIONS
    WHERE flight_number = :new.flight_number; 
    IF n > 0 THEN raise_application_error
        (-20200, 'You cant buy a ticket for a cancelled flight');
    END IF;

    --Получаем номер рейса и дату
    SELECT flight_number, flight_date
    INTO flight, flight_date
    FROM FLIGHTS 
    WHERE flight_number = :new.flight_number;

    OPERATON_DATE_CHECK_PROCEDURE(:new.ticket_id, :new.operation_date, flight, flight_date);
END;
/


CREATE OR REPLACE TRIGGER TICKET_DATE_CHECK_UPDATE
BEFORE UPDATE
ON TICKETS
FOR EACH ROW 
DECLARE 
    flight   INTEGER;
    flight_date DATE;
    n INTEGER;
    w INTEGER;
BEGIN
    IF :old.operation_date > :new.operation_date THEN
        raise_application_error(-20203, 'WRONG TIME');
    END IF;

    SELECT flight_date
    INTO flight_date FROM FLIGHTS
    WHERE flight_number = :new.flight_number;

    --Проверяем, что рейс не отменен
    SELECT COUNT(*)
    INTO n FROM CANCELLATIONS
    WHERE flight_number = :new.flight_number;
 
    IF n > 0 THEN raise_application_error
        (-20200, 'You cant buy a ticket for a cancelled flight');
    END IF;

    --Получаем номер рейса и дату
    SELECT flight_number, flight_date
    INTO flight, flight_date
    FROM FLIGHTS 
    WHERE flight_number = :new.flight_number;

    OPERATON_DATE_CHECK_PROCEDURE(:new.ticket_id, :new.operation_date, flight, flight_date);
END;
/