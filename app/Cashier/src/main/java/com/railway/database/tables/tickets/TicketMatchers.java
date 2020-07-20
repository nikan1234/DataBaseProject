package com.railway.database.tables.tickets;

import com.railway.database.Matcher;

import java.time.LocalDate;
import java.util.Objects;

public class TicketMatchers {
    static public class DateInterval {
        LocalDate from;
        LocalDate to;

        public DateInterval(LocalDate from, LocalDate to) {
            this.from = from;
            this.to = to;
        }
    }

    static public class MatchByPassengerAge extends Matcher {
        @Override
        public String getCondition() {
            return TicketDomains.Passenger.AGE
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByPassengerGender extends Matcher {
        private char gender = 0;

        @Override
        public Matcher bind(Object o) {
            if (o instanceof String) {
                gender = ((String)o).toUpperCase().charAt(0);
            }
            return this;
        }

        @Override
        public String getCondition() {
            if (gender == 0)
                return TicketDomains.Passenger.GENDER + comparator() + '?';
            return TicketDomains.Passenger.GENDER
                    + comparator()
                    + Matcher.format(Character.toString(gender));
        }
    }

    static public class MatchByPurchase extends Matcher {
        char purchased = 0x00;
        @Override
        public Matcher bind(Object o) {
            if (o instanceof  Boolean) {
                boolean b = (Boolean) o;
                purchased = (b) ? 'Y' : 'N';
            }
            return this;
        }

        @Override
        public String getCondition() {
            if (purchased == 0x00)
                return TicketDomains.Passenger.GENDER + EQUAL + '?';
            return TicketDomains.PURCHASED
                    + EQUAL + Matcher.format(Character.toString(purchased));
        }
    }

    static public class MatchByPassengerBaggage extends Matcher {
        private char hasBaggage = 0;

        @Override
        public Matcher bind(Object o) {
            if (o instanceof Boolean) {
                hasBaggage = ((Boolean) o) ? 'Y' : 'N';
            }
            return this;
        }

        @Override
        public String getCondition() {
            if (hasBaggage == 0)
                return TicketDomains.BAGGAGE + EQUAL + '?';
            return TicketDomains.BAGGAGE
                    + EQUAL
                    + Matcher.format(Character.toString(hasBaggage));
        }
    }

    static public class MatchByPurchaseDay extends Matcher {
        @Override
        public String getCondition() {
            return "TO_CHAR("
                    + TicketDomains.OPERATION_DATE
                    + ", 'YYYY-MM-DD')"
                    + EQUAL
                    + Objects.requireNonNullElse(
                    Matcher.format(getValue().toString()), '?');
        }
    }

    static public class MatchByPurchaseInterval extends Matcher {
        private DateInterval dateInterval;

        @Override
        public Matcher bind(Object o) {
            if (o instanceof DateInterval) {
                dateInterval = (DateInterval) o;
            }
            return this;
        }

        private String formatDate(LocalDate d) {
            return "TO_DATE(" + Matcher.format(d.toString()) + ", 'YYYY-MM-DD')";
        }

        @Override
        public String getCondition() {
            if (dateInterval == null)
                return TicketDomains.OPERATION_DATE + "BETWEEN ? AND ?";
            return TicketDomains.OPERATION_DATE
                    + " BETWEEN "
                    + formatDate(dateInterval.from) + " AND "
                    + formatDate(dateInterval.to);
        }
    }
}
