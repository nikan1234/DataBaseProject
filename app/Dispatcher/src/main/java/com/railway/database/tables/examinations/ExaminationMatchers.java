package com.railway.database.tables.examinations;

import com.railway.database.Matcher;
import com.railway.database.tables.employee.EmployeeDomains;

import java.util.Objects;

public class ExaminationMatchers {

    static public class MatchByPatientId extends Matcher {
        @Override
        public String getCondition() {
            return ExaminationDomains.PATIENT_ID
                    + comparator() + Objects.requireNonNullElse(getValue(), '?');
        }
    }

    static public class MatchByExaminationYear extends Matcher {
        @Override
        public String getCondition() {
            final String formatString =
                    "%s %s (SELECT DISTINCT %s " +
                            "FROM MEDICAL_EXAMINATIONS " +
                            "WHERE EXTRACT(year FROM med_date) = %s)";

            String cmp = comparator().equals(Matcher.EQUAL) ? "IN" : "NOT IN";

            return String.format(formatString,
                    EmployeeDomains.EMPLOYEE_ID,
                    cmp,
                    ExaminationDomains.PATIENT_ID,
                    Objects.requireNonNullElse(getValue(), "?"));
        }
    }
}
