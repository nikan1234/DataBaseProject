package com.railway.database.tables.employee;

public class EmployeeDomains {
    public static final String TABLE_NAME = "EMPLOYEES";
    public static final String DETAILS_TABLE_NAME = "EMPLOYEE_DETAILS_VIEW";
    public static final String EMPLOYEE_ID = "employee_id";
    public static final String EMPLOYEE_LAST_NAME = "last_name";
    public static final String EMPLOYEE_FIRST_NAME = "first_name";
    public static final String EMPLOYEE_SECOND_NAME = "second_name";
    public static final String EMPLOYEE_HIRE_DATE = "hire_date";
    public static final String EMPLOYEE_BIRTH_DATE = "birth_date";
    public static final String EMPLOYEE_GENDER = "gender";
    public static final String EMPLOYEE_CHILD_COUNT = "child_count";
    public static final String EMPLOYEE_SALARY = "salary";
    public static final String EMPLOYEE_SPECIALITY = "speciality";

    public static class Manager {
        public static final String TABLE_NAME = "MANAGER";
    }

    public static class Dispatcher {
        public static final String TABLE_NAME = "DISPATCHER";
    }

    public static class Cashier {
        public static final String TABLE_NAME = "CASHIER";
    }

    public static class Driver {
        public static final String TABLE_NAME = "DRIVER";
    }

    public static class Repairman {
        public static final String TABLE_NAME = "REPAIRMAN";
        public static final String REPAIRMAN_RANK = "repairman_rank";
    }
}
