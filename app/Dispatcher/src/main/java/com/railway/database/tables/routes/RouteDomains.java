package com.railway.database.tables.routes;

public class RouteDomains {
    public static final String TABLE_NAME = "ROUTES";
    public static final String ROUTE_ID = "route_id";
    public static final String START_STATION = "start_station";
    public static final String FINISH_STATION = "finish_station";
    public static final String ROUTE_TYPE = "route_type";

    public static class Types {
        public static final String TABLE_NAME = "ROUTE_TYPES";
        public static final String ROUTE_TYPE = "route_type";
    }

    public static class History {
        public static final String TABLE_NAME = "ROUTE_INFO";
        public static final String STATION_FROM = "station_from";
        public static final String STATION_TO = "station_to";
        public static final String CURRENT_TIME = "cur_time";
    }
}
