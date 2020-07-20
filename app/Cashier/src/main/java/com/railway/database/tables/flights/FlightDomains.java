package com.railway.database.tables.flights;

public class FlightDomains {
    public static final String TABLE_NAME = "FLIGHTS";
    public static final String FLIGHT_NUMBER = "flight_number";
    public static final String FLIGHT_DATE = "flight_date";
    public static final String TICKET_COST = "ticket_cost";


    static public class Timetable {
        public static final String TABLE_NAME = "FLIGHTS_TIMETABLE";
        public static final String START_STATION_NAME = "start_station_name";
        public static final String FINISH_STATION_NAME = "finish_station_name";
        public static final String FLIGHT_DURATION = "duration";
        public static final String START_TIME = "start_time";
        public static final String ARRIVAL_TIME = "arrival_time";

        public static final String HAS_DELAY = "has_delay";
        public static final String DELAY_CAUSE = "delay_cause";
        public static final String DELAY_DURATION = "delay_duration";

        public static final String HAS_CANCELLATION = "has_cancellation";
        public static final String CANCEL_CAUSE     = "cancel_cause";
    }
}
