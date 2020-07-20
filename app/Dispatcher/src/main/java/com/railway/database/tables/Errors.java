package com.railway.database.tables;

public class Errors {
    public static final int QUERY_SUCCESS = 0;
    public static final int SAME_DATA_ALREADY_EXISTS = 1;
    public static final int NO_DATA_FOUND = 2;
    public static final int NOT_ENOUGH_DATA = 3;

    public static final int NO_SUCH_VALUE = 1403;

    public static final int TIMETABLE_COLLISION = 20218;

    public static final int LOCOMOTIVE_TEAM_ERROR = 20219;

    public static final int EMPLOYEE_LOCOMOTIVE_DELETE = 20220;

    public static final int REQUIRES_DRIVER = 20222;

    public static final int REQUIRES_REPAIRMAN = 20223;

    public static final int WRONG_OPERATION_TIME = 20224;

    public static final int LOCOMOTIVE_HAS_REPAIR = 20226;

    public static String toString(int errorCode) {
        switch (errorCode) {
            case QUERY_SUCCESS:
                return "SUCCESS";
            case SAME_DATA_ALREADY_EXISTS:
                return "SAME DATA ALREADY EXISTS";
            case NO_DATA_FOUND:
                return "NO DATA FOUND";
            case NOT_ENOUGH_DATA:
                return "NOT ENOUGH DATA";
            case TIMETABLE_COLLISION:
                return "FOUND CONFLICTING FLIGHT\n" +
                        "PLEASE SELECT OTHER LOCOMOTIVE OR REMOVE CONFLICTING FLIGHTS";
            case NO_SUCH_VALUE:
                return "NO SUCH VALUE";
            case LOCOMOTIVE_TEAM_ERROR:
                return "IN LOCOMOTIVE TEAM MUST BE DRIVER AND REPAIRMAN";
            case EMPLOYEE_LOCOMOTIVE_DELETE:
                return "THIS EMPLOYEE IN LOCOMOTIVE TEAM";
            case REQUIRES_DRIVER:
                return "REQUIRES DRIVER FOR LOCOMOTIVE";
            case REQUIRES_REPAIRMAN:
                return "REQUIRES REPAIRMAN FOR LOCOMOTIVE";
            case WRONG_OPERATION_TIME:
                return "WRONG OPERATION TIME";
            case LOCOMOTIVE_HAS_REPAIR:
                return "LOCOMOTIVE HAS REPAIR AT THIS TIME";
            default:
                return "UNKNOWN ERROR " + errorCode;
        }
    }
}
