package com.railway.database.tables.routes;

import com.railway.database.Matcher;

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
}
