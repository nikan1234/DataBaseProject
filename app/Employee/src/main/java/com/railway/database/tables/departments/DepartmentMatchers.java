package com.railway.database.tables.departments;

import com.railway.database.Matcher;

import java.util.Objects;

public class DepartmentMatchers {

    static public class MatchByDepartmentId extends Matcher {
        @Override
        public String getCondition() {
            return DepartmentDomains.DEPARTMENT_ID
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), "?");
        }
    }
}
