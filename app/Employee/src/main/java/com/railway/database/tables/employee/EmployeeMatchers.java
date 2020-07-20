package com.railway.database.tables.employee;

import com.railway.database.Matcher;

import java.util.Objects;

public class EmployeeMatchers {

    static public class MatchById extends Matcher {
        @Override
        public String getCondition() {
            return EmployeeDomains.EMPLOYEE_ID
                    + comparator() + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByIdBeginning extends Matcher {
        @Override
        public String getCondition() {
            if (getValue() == null)
                return "";
            return "TO_CHAR(" + EmployeeDomains.EMPLOYEE_ID
                    + ") LIKE " + format(getValue().toString() + '%');

        }
    }

    static public class MatchByAge extends Matcher {
        @Override
        public String getCondition() {
            return "TRUNC(MONTHS_BETWEEN(sysdate, birth_date) / 12, 0)"
                    + comparator() + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByGender extends Matcher {
        private char gender = 0;
        private String cmp = Matcher.EQUAL;

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
                return EmployeeDomains.EMPLOYEE_GENDER + cmp + '?';
            return EmployeeDomains.EMPLOYEE_GENDER
                    + cmp + format(Character.toString(gender));
        }
    }


    static public class MatchByChildrenCount extends Matcher {
        @Override
        public String getCondition() {
            return EmployeeDomains.EMPLOYEE_CHILD_COUNT
                    + comparator()
                    + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchBySalary extends Matcher {
        @Override
        public String getCondition() {
            return EmployeeDomains.EMPLOYEE_SALARY +
                    comparator() +
                    Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByExperience extends Matcher {
        @Override
        public String getCondition() {
            return "TRUNC(MONTHS_BETWEEN(sysdate, hire_date) / 12, 0)"
                    + comparator() + Objects.requireNonNullElse(getValue(), '?');
        }
    }
}
