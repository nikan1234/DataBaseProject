package com.railway.database.tables.cancellations;

import com.railway.database.Matcher;

public class CancellationMatchers {

    static public class MatchByCancelCause extends Matcher {
        @Override
        public String getCondition() {
            String formatString = "%s LIKE %s";
            return String.format(formatString, CancellationsDomains.CANCEL_CAUSE, format(getValue().toString()));
        }
    }
}
