package com.railway.database.tables;

public class Errors {
    public static final int QUERY_SUCCESS = 0;
    public static final int SAME_DATA_ALREADY_EXISTS = 1;
    public static final int NO_DATA_FOUND = 2;
    public static final int NOT_ENOUGH_DATA = 3;

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
            default:
                return "UNKNOWN ERROR " + errorCode;
        }
    }
}
