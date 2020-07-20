package com.railway.database.tables.flights;

import com.railway.database.Matcher;

import java.util.Objects;

public class FlightMatchers {

    static public class MatchByFlightNumber extends Matcher {
        @Override
        public String getCondition() {
            return FlightDomains.FLIGHT_NUMBER
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByFlightDay extends Matcher {
        @Override
        public String getCondition() {
            return "TO_CHAR("
                    + FlightDomains.FLIGHT_DATE
                    + ", 'YYYY-MM-DD')"
                    + comparator()
                    + Objects.requireNonNullElse(
                            Matcher.format(getValue().toString()), '?');
        }
    }
    static public class ActualFlight extends Matcher {
        @Override
        public String getCondition() {
            String pattern = "TO_DATE(%s, 'DD-MM-YYYY HH24:MI:SS') >= sysdate";
            return String.format(pattern, FlightDomains.Timetable.ARRIVAL_TIME);
        }
    }

    static public class MatchByTicketCost extends Matcher {
        @Override
        public String getCondition() {
            return FlightDomains.TICKET_COST +
                    comparator() +
                    Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByFlightDuration extends Matcher {
        @Override
        public String getCondition() {
            return FlightDomains.Timetable.FLIGHT_DURATION +
                    comparator() +
                    Objects.requireNonNullElse(getValue(), "?");
        }
    }

    static public class MatchByFlightIdBeginning extends Matcher {
        @Override
        public String getCondition() {
            String stringFormat = "TO_CHAR(%s) LIKE %s";
            return String.format(stringFormat, FlightDomains.FLIGHT_NUMBER,
                    format(Objects.requireNonNullElse(getValue(), "?") + "%"));
        }
    }
}
