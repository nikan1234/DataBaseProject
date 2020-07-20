package com.railway.database.tables.delays;

import com.railway.database.Matcher;

public class DelayMatchers {

    static public class MatchByDelayCause extends Matcher {
        @Override
        public String getCondition() {
            String formatString = "%s LIKE %s";
            return String.format(formatString, DelayDomains.DELAY_CAUSE, format(getValue().toString()));
        }
    }
}
