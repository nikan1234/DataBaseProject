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




