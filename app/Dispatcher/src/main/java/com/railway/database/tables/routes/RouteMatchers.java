package com.railway.database.tables.routes;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeDomains;
import com.railway.database.tables.employee.EmployeeMatchers;

import java.util.Objects;

public class RouteMatchers {
    static public class MatchByRouteType extends Matcher {
        @Override
        public String getCondition() {
            return RouteDomains.Types.ROUTE_TYPE
                    + comparator()
                    + Objects.requireNonNullElse(Matcher.format(getValue().toString()), '?');
        }
    }


    static public class MatchByStartStation extends Matcher {
        @Override
        public String getCondition() {
            return RouteDomains.START_STATION
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByFinishStation extends Matcher {
        @Override
        public String getCondition() {
            return RouteDomains.FINISH_STATION
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByRouteId extends Matcher {
        @Override
        public String getCondition() {
            return RouteDomains.ROUTE_ID
                    + comparator()
                    + getValue();
        }
    }

    static public class MatchByDirection extends Matcher {
        @Override
        public String getCondition() {
            String stringFormat =
                    "%s IN (SELECT DISTINCT route_id " +
                            "FROM ROUTE_INFO " +
                            "WHERE station_from  = %d " +
                            "OR    station_to    = %d)";
            Object value = getValue();
            if (value == null) {
                return "";
            }
            return String.format(stringFormat, RouteDomains.ROUTE_ID, value, value);
        }
    }

    static public class MatchByIdBeginning extends Matcher {
        @Override
        public String getCondition() {
            if (getValue() == null)
                return "";
            return "TO_CHAR(" + RouteDomains.ROUTE_ID
                    + ") LIKE " + format(getValue().toString() + '%');

        }
    }
}
