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
}
