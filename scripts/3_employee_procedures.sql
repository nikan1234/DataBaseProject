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