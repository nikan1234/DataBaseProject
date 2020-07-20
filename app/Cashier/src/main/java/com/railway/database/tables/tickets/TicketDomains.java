package com.railway.database.tables.tickets;

public class TicketDomains {
    public static final String TABLE_NAME = "TICKETS";
    public static final String BAGGAGE = "has_baggage";
    public static final String TICKET_ID = "ticket_id";
    public static final String TICKET_COST = "ticket_cost";
    public static final String PURCHASED = "purchased";
    public static final String OPERATION_DATE = "operation_date";

    public static class Passenger {
        public static final String LAST_NAME = "passenger_last_name";
        public static final String FIRST_NAME = "passenger_first_name";
        public static final String SECOND_NAME = "passenger_second_name";
        public static final String AGE = "passenger_age";
        public static final String GENDER = "passenger_gender";
    }
}
