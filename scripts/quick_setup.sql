DROP VIEW FLIGHT_DETAILS;
DROP VIEW FLIGHTS_TIMETABLE;
DROP VIEW ROUTE_DURATIONS;
DROP VIEW ROUTE_INFO;

DROP TABLE CANCELLATIONS;
DROP VIEW DELAY_DETAILS;
DROP TABLE DELAYS;
DROP TABLE TICKETS;
DROP TABLE INSPECTIONS;
DROP TABLE FLIGHTS;
DROP TABLE FLIGHT_TYPES;
DROP TABLE PATHS;
DROP TABLE ROUTES;
DROP TABLE ROUTE_TYPES;
DROP TABLE STATIONS;
DROP TABLE REPAIR;
DROP TABLE LOCOMOTIVES;
DROP TABLE MEDICAL_EXAMINATIONS;
DROP VIEW EMPLOYEE_DETAILS_VIEW;
DROP VIEW REPAIRMAN;
DROP VIEW DRIVER;
DROP VIEW CASHIER;
DROP VIEW DISPATCHER;
DROP VIEW MANAGER;
DROP TABLE MANAGEMENT;
DROP TABLE USERS_DATA;
DROP TABLE EMPLOYEES;
DROP TABLE TEAMS;
DROP TABLE DEPARTMENTS;


CREATE TABLE DEPARTMENTS (
    department_id       NUMBER(4) NOT NULL PRIMARY KEY,
    department_name     VARCHAR(100) NOT NULL
);


CREATE TABLE TEAMS (
    team_id             NUMBER(7) NOT NULL PRIMARY KEY,
    team_type           VARCHAR(100) NOT NULL,
    department_id       INTEGER NOT NULL REFERENCES DEPARTMENTS(department_id) 
                                         ON DELETE CASCADE
);

CREATE TABLE EMPLOYEES (
    employee_id        INTEGER NOT NULL PRIMARY KEY,
    last_name          VARCHAR(100) NOT NULL,
    first_name         VARCHAR(100) NOT NULL, 
    second_name        VARCHAR(100) NOT NULL,
    hire_date          DATE NOT NULL,
    birth_date         DATE NOT NULL,
    gender             CHAR(1)       NOT NULL CHECK(gender IN ('M', 'F')),
    child_count        NUMBER(2)     NOT NULL CHECK(child_count >= 0),
    salary             DECIMAL(10, 2) NOT NULL CHECK(salary > 0),

    speciality         VARCHAR(100) DEFAULT(''),
    team_id            INTEGER REFERENCES TEAMS(team_id) 
                               ON DELETE CASCADE,
    repairman_rank     NUMBER(1) CHECK(repairman_rank BETWEEN 1 AND 5)
);


CREATE TABLE USERS_DATA (
    user_id         INTEGER NOT NULL PRIMARY KEY REFERENCES EMPLOYEES(employee_id)
                                     ON DELETE CASCADE,
    user_password   VARCHAR(64) NOT NULL,
    access_level    NUMBER(1) NOT NULL 
);


CREATE TABLE MANAGEMENT (
    department_id       INTEGER NOT NULL PRIMARY KEY REFERENCES DEPARTMENTS(department_id)
                                         ON DELETE CASCADE,
    employee_id         INTEGER NOT NULL REFERENCES EMPLOYEES(employee_id)
                                         ON DELETE CASCADE
);


CREATE VIEW MANAGER
AS SELECT 
         employee_id, 
         last_name, first_name, second_name, 
         hire_date, 
         birth_date, gender, child_count, 
         salary, 
         speciality
FROM EMPLOYEES 
WHERE speciality = 'Manager'
WITH CHECK OPTION;


CREATE VIEW DISPATCHER 
AS SELECT 
         employee_id, 
         last_name, first_name, second_name, 
         hire_date, 
         birth_date, gender, child_count, 
         salary, 
         speciality, 
         team_id
FROM EMPLOYEES
WHERE speciality = 'Dispatcher'
WITH CHECK OPTION;


CREATE VIEW CASHIER 
AS SELECT 
         employee_id, 
         last_name, first_name, second_name, 
         hire_date, 
         birth_date, gender, child_count, 
         salary, 
         speciality, 
         team_id
FROM EMPLOYEES
WHERE speciality = 'Cashier'
WITH CHECK OPTION;



CREATE VIEW DRIVER 
AS SELECT 
         employee_id, 
         last_name, first_name, second_name, 
         hire_date, 
         birth_date, gender, child_count, 
         salary, 
         speciality, 
         team_id
FROM EMPLOYEES
WHERE speciality = 'Driver'
WITH CHECK OPTION;


CREATE VIEW REPAIRMAN 
AS SELECT 
         employee_id, 
         last_name, first_name, second_name, 
         hire_date, 
         birth_date, gender, child_count, 
         salary, 
         speciality, 
         team_id,
         repairman_rank
FROM EMPLOYEES 
WHERE speciality = 'Repairman'
WITH CHECK OPTION;

--VIEW для отображения работников
--Прибивает менеджера отдела к отделу
CREATE VIEW EMPLOYEE_DETAILS_VIEW AS 
SELECT E.employee_id, 
       E.last_name, 
       E.first_name, 
       E.second_name,
       E.hire_date,
       E.birth_date,
       E.gender,
       E.child_count,
       E.salary,
       E.speciality,
       T.team_id,
       department_id
FROM DEPARTMENTS D INNER JOIN TEAMS T USING(department_id)
                   INNER JOIN EMPLOYEES E 
                   ON T.team_id = E.team_id
UNION
SELECT E.employee_id, 
       E.last_name,
       E.first_name,
       E.second_name,
       E.hire_date,
       E.birth_date,
       E.gender,
       E.child_count,
       E.salary,
       E.speciality,
       NULL AS team_id,
       M.department_id
FROM MANAGER E LEFT JOIN MANAGEMENT M 
               ON E.employee_id = M.employee_id
ORDER BY employee_id;



CREATE TABLE MEDICAL_EXAMINATIONS (
    med_id              INTEGER NOT NULL PRIMARY KEY,
    employee_id         INTEGER NOT NULL REFERENCES EMPLOYEES(employee_id)
                                         ON DELETE CASCADE,
    med_date            DATE NOT NULL
);


CREATE TABLE LOCOMOTIVES (
    locomotive_id       INTEGER NOT NULL PRIMARY KEY,
    locomotive_name     VARCHAR(100) NOT NULL,
    team_id             INTEGER REFERENCES TEAMS(team_id)
                                     ON DELETE SET NULL,
    entry_date          DATE NOT NULL
);


CREATE TABLE REPAIR (
    repair_id       INTEGER NOT NULL PRIMARY KEY,
    locomotive_id   INTEGER NOT NULL REFERENCES LOCOMOTIVES(locomotive_id) 
                                     ON DELETE CASCADE,
    repair_date     DATE NOT NULL
);


CREATE TABLE STATIONS (
    station_id      INTEGER NOT NULL PRIMARY KEY,
    station_name    VARCHAR(100) NOT NULL,

    CONSTRAINT st_unique UNIQUE(station_name)
);

CREATE TABLE ROUTE_TYPES (
    route_type VARCHAR(100) NOT NULL PRIMARY KEY
);

CREATE TABLE ROUTES (
    route_id            INTEGER NOT NULL PRIMARY KEY,
    start_station       INTEGER NOT NULL REFERENCES STATIONS(station_id)
                                         ON DELETE CASCADE,

    finish_station       INTEGER NOT NULL REFERENCES STATIONS(station_id)
                                          ON DELETE CASCADE,
    route_type           NOT NULL REFERENCES ROUTE_TYPES(route_type)
                                  ON DELETE CASCADE
);


CREATE TABLE PATHS (
    path_id             INTEGER NOT NULL PRIMARY KEY,
    station_from        INTEGER NOT NULL REFERENCES STATIONS(station_id)
                                         ON DELETE CASCADE,
    station_to          INTEGER NOT NULL REFERENCES STATIONS(station_id)
                                         ON DELETE CASCADE,

    --сколько ехать от station_from до station_to в часах
    time_shift          NUMBER NOT NULL,

    route_id            INTEGER NOT NULL REFERENCES ROUTES(route_id)
                                         ON DELETE CASCADE,

    CONSTRAINT no_loops CHECK(station_from <> station_to)
);


CREATE TABLE FLIGHT_TYPES ( 
    flight_type VARCHAR(100) NOT NULL PRIMARY KEY
);

CREATE TABLE FLIGHTS (
    flight_number       INTEGER NOT NULL PRIMARY KEY,
    flight_date         DATE    NOT NULL, 
    locomotive_id       INTEGER NOT NULL REFERENCES LOCOMOTIVES(locomotive_id)
                                         ON DELETE CASCADE,
    route_id            INTEGER NOT NULL REFERENCES ROUTES(route_id)
                                         ON DELETE CASCADE,
    ticket_cost         DECIMAL(10, 2) NOT NULL,
    flight_type         VARCHAR(100)  REFERENCES FLIGHT_TYPES(flight_type)
                                                 ON DELETE SET NULL
);  

-- Подготовка в рейсу (может не проводиться)
CREATE TABLE INSPECTIONS (
    inspection_id       INTEGER NOT NULL PRIMARY KEY,
    flight_number       INTEGER NOT NULL UNIQUE REFERENCES FLIGHTS(flight_number) 
                                                ON DELETE CASCADE,
    inspection_date     DATE NOT NULL
);


CREATE TABLE TICKETS (
    ticket_id                   VARCHAR(256) NOT NULL PRIMARY KEY,
    passenger_last_name         VARCHAR(100) NOT NULL,
    passenger_first_name        VARCHAR(100) NOT NULL, 
    passenger_second_name       VARCHAR(100) NOT NULL,

    has_baggage                 CHAR(1)   NOT NULL CHECK(has_baggage IN ('N','Y')),
    passenger_age               NUMBER(3) NOT NULL CHECK(passenger_age > 0),
    passenger_gender            CHAR(1)   NOT NULL CHECK(passenger_gender IN ('M', 'F')),

    flight_number               INTEGER NOT NULL REFERENCES FLIGHTS(flight_number)
                                                 ON DELETE CASCADE,

    purchased                   CHAR(1) NOT NULL CHECK (purchased IN ('N','Y')),

    --Время покупки или бронирования билета
    operation_date              DATE NOT NULL
);

CREATE TABLE DELAYS (
    delay_id        INTEGER NOT NULL PRIMARY KEY,
    flight_number   INTEGER UNIQUE REFERENCES FLIGHTS(flight_number)
                                            ON DELETE SET NULL,
    delay_cause     VARCHAR(256) NOT NULL,
    
    -- duration in hours
    delay_duration  INTEGER NOT NULL CHECK(delay_duration > 0)

);


CREATE VIEW DELAY_DETAILS AS
SELECT flight_number, 
       flight_type,  
       ticket_cost, 
       route_id, 
       locomotive_id, 
       delay_cause, 
       delay_duration,
       TO_CHAR(flight_date, 'DD-MM-YYYY HH24:MI:SS') flight_date,
       TO_CHAR(flight_date + delay_duration / 24, 'DD-MM-YYYY HH24:MI:SS') delayed_until 
FROM FLIGHTS INNER JOIN DELAYS USING(flight_number);


CREATE TABLE CANCELLATIONS (
    cancel_id       INTEGER NOT NULL PRIMARY KEY,
    flight_number   INTEGER UNIQUE REFERENCES FLIGHTS(flight_number)
                                            ON DELETE SET NULL,
    cancel_cause    VARCHAR(256) NOT NULL
);


-- Таблица маршрутов:
-- перечислены все станции на маршруте
CREATE VIEW ROUTE_INFO AS
WITH NAMED_PATHS (route_id, station_from_id, station_from_name, station_to_id, station_to_name, time_shift) AS (
    SELECT P.route_id, P.station_from, F.station_name, P.station_to, T.station_name, P.time_shift
    FROM STATIONS F INNER JOIN PATHS    P  ON F.station_id = P.station_from
                    INNER JOIN STATIONS T  ON T.station_id = P.station_to
),
RECURSION(route_id, station_from, station_to, cur_way, cur_time) AS (
    SELECT N.route_id, 
           N.station_from_id, 
           N.station_to_id, 
           N.station_from_name || ' - ' || N.station_to_name,
           N.time_shift
    FROM   NAMED_PATHS N INNER JOIN ROUTES R ON  N.station_from_id = R.start_station 
                                             AND N.route_id        = R.route_id

        UNION ALL
    SELECT R.route_id, 
           R.station_from, 
           P.station_to_id, R.cur_way|| ' - ' || P.station_to_name, 
           R.cur_time + P.time_shift
    FROM NAMED_PATHS P INNER JOIN RECURSION R
    ON  R.station_to  = P.station_from_id 
    AND R.route_id    = P.route_id
)
CYCLE station_to SET cyclemark TO 'X' DEFAULT '-'
SELECT *  
FROM RECURSION
ORDER BY route_id, cur_time;


-- Для подсчета длительности маршрута 
-- (в часах)
CREATE VIEW ROUTE_DURATIONS AS
SELECT R.route_id, R.route_type, 
       R.start_station, R.finish_station,
       I.CUR_TIME AS route_duration
FROM ROUTES R INNER JOIN ROUTE_INFO I 
ON R.start_station = I.station_from 
AND R.finish_station = I.station_to
AND R.route_id = I.route_id;
                          

--Получить список рейсов: номер рейса, начальная и конечная станции, время отправления/прибытия
--Задержки/отмены рейсов и причины
CREATE VIEW FLIGHTS_TIMETABLE AS
SELECT F.FLIGHT_NUMBER, 
       F.TICKET_COST,
       F.LOCOMOTIVE_ID,
       route_id,
       SFROM.station_name START_STATION_NAME, 
       STO.station_name   FINISH_STATION_NAME, 
       TO_CHAR(flight_date, 'DD-MM-YYYY HH24:MI:SS') START_TIME,
       TO_CHAR(flight_date + CUR_TIME / 24, 'DD-MM-YYYY HH24:MI:SS') ARRIVAL_TIME,
       CUR_TIME AS DURATION,
       DECODE(D.HAS_DELAY, 1, 'Y', 'N') HAS_DELAY,
       D.DELAY_CAUSE,
       D.DELAY_DURATION,
       DECODE(C.HAS_CANCELLATION, 1, 'Y', 'N') HAS_CANCELLATION,
       C.CANCEL_CAUSE
FROM  
ROUTE_INFO R INNER JOIN FLIGHTS F USING(route_id)
             INNER JOIN STATIONS SFROM  ON R.station_from = SFROM.station_id
             INNER JOIN STATIONS STO    ON R.station_to   = STO.station_id
             INNER JOIN ROUTES          USING(route_id)
             INNER JOIN (SELECT F.flight_number, D.delay_cause, D.delay_duration, count(D.flight_number) HAS_DELAY
			             FROM DELAYS D RIGHT JOIN FLIGHTS F ON D.flight_number = F.flight_number
			             GROUP BY F.flight_number, D.delay_cause, D.delay_duration) D 
             ON D.flight_number = F.flight_number

             INNER JOIN (SELECT F.flight_number, C.cancel_cause, count(C.flight_number) HAS_CANCELLATION
			             FROM CANCELLATIONS C RIGHT JOIN FLIGHTS F ON C.flight_number = F.flight_number
			             GROUP BY F.flight_number, C.cancel_cause) C 
             ON C.flight_number = F.flight_number

WHERE ROUTES.start_station = R.station_from AND ROUTES.finish_station = R.station_to;


--Для каждого маршрута получим все станции, через которые мы проходим и время прибытия туда
CREATE VIEW FLIGHT_DETAILS AS
SELECT FLIGHT_NUMBER , 
       STATION_ID,
       STATION_NAME, 
       TO_CHAR(flight_date + CUR_TIME / 24, 'DD-MM-YYYY HH24:MI:SS') ARRIVAL_TIME
FROM 
ROUTE_INFO R INNER JOIN FLIGHTS  F USING(route_id)
             INNER JOIN STATIONS S ON station_to = station_id

    UNION 

SELECT FLIGHT_NUMBER, 
       STATION_ID,
       STATION_NAME, 
       TO_CHAR(flight_date, 'DD-MM-YYYY HH24:MI:SS') ARRIVAL_TIME
FROM FLIGHTS INNER JOIN ROUTES     USING(route_id)
             INNER JOIN STATIONS S ON start_station = station_id;






-- TEAMS 
CREATE OR REPLACE TRIGGER TEAM_ID_EDITOR 
BEFORE INSERT
   ON TEAMS
   FOR EACH ROW
DECLARE 
   n        NUMBER(3);
   max_id   NUMBER(7);
BEGIN
   SELECT COUNT(*)
   INTO n FROM TEAMS 
   WHERE department_id = :new.department_id;
   IF n = 0 THEN
      :new.team_id := :new.department_id * 1000 + 1;
      RETURN;
   END IF;

   SELECT max(team_id)
   INTO max_id FROM TEAMS 
   WHERE department_id = :new.department_id;

   :new.team_id := max_id + 1;
END;
/


-- MEDICAL_EXAMINATIONS
DROP SEQUENCE MEDICAL_EX_SEQUENCE;
CREATE SEQUENCE MEDICAL_EX_SEQUENCE minvalue 0;

CREATE OR REPLACE TRIGGER MEDICAL_EX_AUTOINCR
BEFORE INSERT
   ON MEDICAL_EXAMINATIONS
   FOR EACH ROW
BEGIN
     :new.med_id := MEDICAL_EX_SEQUENCE.NextVal;
END;
/



-- INSPECTION
DROP SEQUENCE INSPECTION_SEQUENCE;
CREATE SEQUENCE INSPECTION_SEQUENCE minvalue 0;

CREATE OR REPLACE TRIGGER INSPECTION_AUTOINCR
BEFORE INSERT
   ON INSPECTIONS
   FOR EACH ROW
BEGIN
     :new.inspection_id := INSPECTION_SEQUENCE.NextVal;
END;
/


-- PATHS
DROP SEQUENCE PATH_SEQUENCE;
CREATE SEQUENCE PATH_SEQUENCE minvalue 0;

CREATE OR REPLACE  TRIGGER PATH_AUTOINCR
BEFORE INSERT
   ON PATHS
   FOR EACH ROW
BEGIN
     :new.path_id := PATH_SEQUENCE.NextVal;
END;
/
   

CREATE OR REPLACE PROCEDURE ADD_MANAGER(
    manager_id          IN INTEGER,
    last_name           IN VARCHAR,
    first_name          IN VARCHAR, 
    second_name         IN VARCHAR, 
    hire_date           IN VARCHAR,
    birth_date          IN VARCHAR,
    gender              IN CHAR,
    child_count         IN NUMBER, 
    salary              IN DECIMAL,
    manager_password    IN VARCHAR
) AS 
    BEGIN
        INSERT INTO MANAGER VALUES(
           manager_id, 
           last_name, first_name, second_name,
           TO_DATE(hire_date, 'YYYY-MM-DD'), 
           TO_DATE(birth_date, 'YYYY-MM-DD'), 
           gender, 
           child_count, 
           salary,
           'Manager'); 
        INSERT INTO USERS_DATA VALUES(
            manager_id, manager_password, 4
        );
END ADD_MANAGER; 
/


CREATE OR REPLACE PROCEDURE ADD_DISPATCHER
(
 employee_id       IN INTEGER,
 last_name         IN VARCHAR,
 first_name        IN VARCHAR, 
 second_name       IN VARCHAR, 
 hire_date         IN VARCHAR,
 birth_date        IN VARCHAR,
 gender            IN CHAR,
 child_count       IN NUMBER, 
 salary            IN DECIMAL, 
 team_id           IN INTEGER,
 employee_password IN VARCHAR
 ) AS 
    BEGIN
       INSERT INTO DISPATCHER VALUES(
           employee_id, 
           last_name, first_name, second_name,
           TO_DATE(hire_date, 'YYYY-MM-DD'), 
           TO_DATE(birth_date, 'YYYY-MM-DD'), 
           gender, 
           child_count, 
           salary, 
           'Dispatcher', 
           team_id);

        INSERT INTO USERS_DATA VALUES(
            employee_id, employee_password, 3
        ); 
END ADD_DISPATCHER; 
/



CREATE OR REPLACE PROCEDURE ADD_CASHIER
(
 employee_id       IN INTEGER,
 last_name         IN VARCHAR,
 first_name        IN VARCHAR, 
 second_name       IN VARCHAR, 
 hire_date         IN VARCHAR,
 birth_date        IN VARCHAR,
 gender            IN CHAR,
 child_count       IN NUMBER, 
 salary            IN DECIMAL, 
 team_id           IN INTEGER,
 employee_password IN VARCHAR
 ) AS 
    BEGIN
       INSERT INTO CASHIER VALUES(
           employee_id, 
           last_name, first_name, second_name,
           TO_DATE(hire_date, 'YYYY-MM-DD'), 
           TO_DATE(birth_date, 'YYYY-MM-DD'), 
           gender, 
           child_count, 
           salary, 
           'Cashier', 
           team_id); 

        INSERT INTO USERS_DATA VALUES(
            employee_id, employee_password, 2
        ); 
END ADD_CASHIER; 
/


CREATE OR REPLACE PROCEDURE ADD_DRIVER
(
 employee_id       IN INTEGER,
 last_name         IN VARCHAR,
 first_name        IN VARCHAR, 
 second_name       IN VARCHAR, 
 hire_date         IN VARCHAR,
 birth_date        IN VARCHAR,
 gender            IN CHAR,
 child_count       IN NUMBER, 
 salary            IN DECIMAL, 
 team_id           IN INTEGER,
 employee_password IN VARCHAR
 ) AS 
    BEGIN
       INSERT INTO DRIVER VALUES(
           employee_id, 
           last_name, first_name, second_name,
           TO_DATE(hire_date, 'YYYY-MM-DD'), 
           TO_DATE(birth_date, 'YYYY-MM-DD'), 
           gender, 
           child_count, 
           salary, 
           'Driver', 
           team_id);

        INSERT INTO USERS_DATA VALUES(
            employee_id, employee_password, 1
        );  
END ADD_DRIVER;   
/


CREATE OR REPLACE PROCEDURE ADD_REPAIRMAN 
(
    employee_id       IN INTEGER,
   last_name         IN VARCHAR,
   first_name        IN VARCHAR, 
   second_name       IN VARCHAR, 
   hire_date         IN VARCHAR,
   birth_date        IN VARCHAR,
   gender            IN CHAR,
   child_count       IN NUMBER, 
   salary            IN DECIMAL, 
   team_id           IN INTEGER,
   repairman_rank    IN NUMBER,
   employee_password IN VARCHAR
) AS 
   BEGIN
     INSERT INTO REPAIRMAN VALUES(
           employee_id, 
           last_name, first_name, second_name,
           TO_DATE(hire_date, 'YYYY-MM-DD'), 
           TO_DATE(birth_date, 'YYYY-MM-DD'), 
           gender, 
           child_count, 
           salary, 
           'Repairman', 
           team_id,
           repairman_rank);

        INSERT INTO USERS_DATA VALUES(
            employee_id, employee_password, 1
        );  
END ADD_REPAIRMAN;
/


CREATE OR REPLACE TRIGGER MANAGER_CHECKER
BEFORE INSERT
   ON MANAGEMENT
   FOR EACH ROW
DECLARE 
   manager_ INTEGER;
BEGIN
   SELECT count(*) 
   INTO manager_ FROM MANAGER 
   WHERE employee_id = :new.employee_id;

   IF manager_ IS NULL THEN
        raise_application_error
            (-20214, 'Not manager');
   END IF;
END MANAGER_CHECKER;
/



​
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


BEGIN
    ADD_MANAGER(
        1001, 'Долгов', 'Никита', 'Юрьевич', 
        '2003-02-01',
        '1980-10-25', 'M', 0, 120000.00, '123456');

    ADD_MANAGER(
        1002, 'Петров', 'Евгений', 'Сергеевич', 
        '2002-04-15',
        '1976-04-09', 'M', 0, 80000.00, 'Hello');

    ADD_MANAGER(
        1003, 'Малаева', 'Екатерина', 'Андреевна', 
        '2012-07-08',
        '1979-04-19', 'F', 0, 15000.00, 'LOL1234');
END;
/

INSERT INTO DEPARTMENTS VALUES(7000, 'Отдел логистики');
INSERT INTO DEPARTMENTS VALUES(8000, 'Отдел транспорта');
INSERT INTO DEPARTMENTS VALUES(9000, 'Отдел обслуживания');

INSERT INTO MANAGEMENT VALUES(7000, 1001);
INSERT INTO MANAGEMENT VALUES(8000, 1001);
INSERT INTO MANAGEMENT VALUES(9000, 1002); 


--Первые 4 цифры id - номер бригады, 
--следующие 3 - номер команды в этой бригаде
INSERT INTO TEAMS VALUES(0, 'Бригада диспетчеров',      7000);
INSERT INTO TEAMS VALUES(0, 'Локомотивная бригада №1',  8000);
INSERT INTO TEAMS VALUES(0, 'Локомотивная бригада №2',  8000);
INSERT INTO TEAMS VALUES(0, 'Бригада кассиров',         9000);

BEGIN
    ADD_DISPATCHER(10001, 'Евтушенко', 'Василий', 'Андреевич',
    '2003-02-01', '1941-08-03', 'M', 3, 48000.00, 7000001, 'Vasya1976');
    
    ADD_CASHIER(20001, 'Маркова', 'Ирина', 'Петровна',
    '2012-09-26', '1982-07-19', 'F', 0, 19000.00, 8000001, 'Itanium228');

    ADD_DRIVER(30001, 'Лусников', 'Василий', 'Олегович',
    '2007-04-12', '1979-11-06', 'M', 1, 28500.00, 8000001, '123hihi321');

    ADD_DRIVER(30002, 'Матросов', 'Анатолий', 'Юрьевич',
    '2006-03-01', '1996-04-28', 'M', 4, 20000.00, 8000001, 'Tolyan322');

    ADD_REPAIRMAN(40001, 'Лысый', 'Артем', 'Андреевич', 
    '2007-02-09', '1992-02-07', 'M', 2, 56000.00, 8000001, 4, 'BrrrBrrr');


    ADD_DRIVER(30003, 'Евсюков', 'Аркадий', 'Юрьевич',
    '2016-05-01', '1992-04-18', 'M', 4, 25000.00, 8000002,'Toу33');

    ADD_REPAIRMAN(40002, 'Камазов', 'Алексей', 'Андреевич', 
    '2011-02-19', '1995-06-27', 'M', 2, 26000.00, 8000002, 2, 'TruckOfsht');
END;
/


INSERT INTO LOCOMOTIVES VALUES(3400, 'Победа', 8000001, TO_DATE('1999-05-01', 'YYYY-MM-DD'));
INSERT INTO LOCOMOTIVES VALUES(8240, 'Центурион', 8000001, TO_DATE('2002-04-28', 'YYYY-MM-DD'));
INSERT INTO LOCOMOTIVES VALUES(6210, 'Транзит-66', 8000002, TO_DATE('2003-03-16', 'YYYY-MM-DD'));


INSERT INTO REPAIR VALUES(0, 3400, TO_DATE('2008-04-05', 'YYYY-MM-DD'));
INSERT INTO REPAIR VALUES(0, 3400, TO_DATE('2020-05-22', 'YYYY-MM-DD'));
INSERT INTO REPAIR VALUES(0, 8240, TO_DATE('2020-05-15', 'YYYY-MM-DD'));


INSERT INTO STATIONS VALUES(1, 'Москва');
INSERT INTO STATIONS VALUES(2, 'Омск');
INSERT INTO STATIONS VALUES(3, 'Новосибирск');
INSERT INTO STATIONS VALUES(4, 'Барнаул');
INSERT INTO STATIONS VALUES(5, 'Севастополь');
INSERT INTO STATIONS VALUES(6, 'Берлин');
INSERT INTO STATIONS VALUES(7, 'Минск');
INSERT INTO STATIONS VALUES(8, 'Варшава'); 

INSERT INTO ROUTE_TYPES VALUES('Внутренний');
INSERT INTO ROUTE_TYPES VALUES('Международный');
INSERT INTO ROUTE_TYPES VALUES('Туристический');
INSERT INTO ROUTE_TYPES VALUES('Специальный');

INSERT INTO ROUTES VALUES(101, 1, 2, 'Внутренний');
INSERT INTO PATHS VALUES(0, 1, 3, 72, 101);
INSERT INTO PATHS VALUES(0, 3, 2, 10, 101);

INSERT INTO ROUTES VALUES(201, 4, 6, 'Международный');
INSERT INTO PATHS VALUES(0, 4, 3, 5, 201);
INSERT INTO PATHS VALUES(0, 3, 1, 64, 201);
INSERT INTO PATHS VALUES(0, 1, 6, 120, 201);

INSERT INTO ROUTES VALUES(301, 3, 5, 'Туристический');
INSERT INTO PATHS VALUES(0, 3, 1, 80, 301);
INSERT INTO PATHS VALUES(0, 1, 5, 48, 301);

INSERT INTO ROUTES VALUES(401, 3, 4, 'Специальный');
INSERT INTO PATHS VALUES(0, 3, 4, 5, 401);

INSERT INTO ROUTES VALUES(501, 5, 8, 'Международный');
INSERT INTO PATHS VALUES(0, 5, 7, 10, 501);
INSERT INTO PATHS VALUES(0, 7, 8, 25, 501);


INSERT INTO FLIGHT_TYPES VALUES('Скорый');
INSERT INTO FLIGHT_TYPES VALUES('Пассажирский');


INSERT INTO MEDICAL_EXAMINATIONS VALUES(0, 30003, TO_DATE('2018-02-01', 'YYYY-MM-DD'));
INSERT INTO MEDICAL_EXAMINATIONS VALUES(0, 30003, TO_DATE('2020-02-01', 'YYYY-MM-DD'));
INSERT INTO MEDICAL_EXAMINATIONS VALUES(0, 30001, TO_DATE('2019-05-15', 'YYYY-MM-DD'));


INSERT INTO FLIGHTS VALUES(
    1001, TO_DATE('2020-05-11 13:00', 'YYYY-MM-DD HH24:MI'), 
    3400, 201, 4500.00, 'Скорый');

INSERT INTO FLIGHTS VALUES(
    2005, TO_DATE('2020-05-15 18:45', 'YYYY-MM-DD HH24:MI'), 
    8240, 101, 2900.50, 'Пассажирский');

INSERT INTO FLIGHTS VALUES(
    3201, TO_DATE('2020-06-01 14:00', 'YYYY-MM-DD HH24:MI'), 
    6210, 401, 12000.0, 'Скорый');


INSERT INTO INSPECTIONS VALUES(0, 1001, TO_DATE('2020-05-11', 'YYYY-MM-DD'));
INSERT INTO INSPECTIONS VALUES(0, 3201, TO_DATE('2020-06-01', 'YYYY-MM-DD'));

INSERT INTO DELAYS VALUES(0, 1001, 'Плохая погода', 46);


INSERT INTO TICKETS VALUES(
    '', 'Овальный', 'Алексей', 'Сергеевич', 'Y', 34, 'M', 1001, 'Y', 
    TO_DATE('2020-05-11 12:45:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO TICKETS VALUES(
    '', 'Перепелкин', 'Вадим', 'Евгеньевич', 'Y', 19, 'M', 1001, 'N', 
    TO_DATE('2020-05-11 11:05:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO TICKETS VALUES(
    '', 'Колбасов', 'Валерий', 'Евгеньевич', 'Y', 48, 'M', 1001, 'Y', 
    TO_DATE('2020-05-11 18:05:00', 'YYYY-MM-DD HH24:MI:SS'));


INSERT INTO TICKETS VALUES(
    '', 'Макарова', 'Алина', 'Васильевна', 'Y', 26, 'F', 1001, 'Y', 
    TO_DATE('2020-05-12 10:05:00', 'YYYY-MM-DD HH24:MI:SS'));



INSERT INTO TICKETS VALUES(
    '', 'Говоров', 'Аркадий', 'Юрьевич', 'Y', 22, 'M', 2005, 'N', 
    TO_DATE('2020-05-14 16:10:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO TICKETS VALUES(
    '', 'Маркова', 'Наталья', 'Петровна', 'N', 67, 'F', 2005, 'Y', 
    TO_DATE('2020-05-15 11:10:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO TICKETS VALUES(
    '', 'Александров', 'Александр', 'Петрович', 'Y', 55, 'M', 2005, 'Y', 
    TO_DATE('2020-05-13 19:10:00', 'YYYY-MM-DD HH24:MI:SS'));



INSERT INTO TICKETS VALUES(
    '', 'Морозов', 'Михаил', 'Олегович', 'Y', 19, 'M', 3201, 'Y', 
    TO_DATE('2020-05-13 19:10:00', 'YYYY-MM-DD HH24:MI:SS'));

INSERT INTO TICKETS VALUES(
    '', 'Баранов', 'Алексей', 'Юрьевич', 'Y', 27, 'M', 3201, 'N', 
    TO_DATE('2020-05-20 15:23:00', 'YYYY-MM-DD HH24:MI:SS'));


INSERT INTO CANCELLATIONS VALUES(0, 3201, 'Поломка локомотива');






