package com.railway.database.tables;

public class ErrorCodes {
    /**
     * В случае отмены рейса, при попытке приобретения билета
     */
    public static final int FLIGHT_CANCELLED = 20200;

    /**
     * В случае, если время покупки или возвращения билета позднее,
     * чем время отправления рейса
     */
    public static final int FLIGHT_MISSED    = 20201;

    /**
     * При попытке оплатить билет, которого нет
     */
    public static final int NO_SUCH_TICKET = 20202;

    public static final int WRONG_TIME = 20203;

    public static String toString(int errorCode) {
        StringBuilder builder = new StringBuilder();
        builder.append("REASON: ");

        switch (errorCode) {
            case FLIGHT_CANCELLED: builder.append("FLIGHT CANCELLED");
                break;
            case FLIGHT_MISSED: builder.append("FLIGHT HAVE ALREADY STARTED");
                break;
            case NO_SUCH_TICKET: builder.append("TICKET NOT FOUND");
                break;
            case WRONG_TIME: builder.append("WRONG TIME");
                break;
            default: builder.append("UNKNOWN ERROR ").append(errorCode);
        }
        return builder.toString();
    }
}
