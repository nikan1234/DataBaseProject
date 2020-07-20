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






