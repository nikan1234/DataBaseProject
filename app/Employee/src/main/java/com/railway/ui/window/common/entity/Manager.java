package com.railway.ui.window.common.entity;

import java.time.LocalDate;

public class Manager extends Employee {
    public static final String specName = Specialty.MANAGER;

    public Manager(int id,
                   String lastName,
                   String firstName,
                   String secondName,
                   LocalDate birthDate,
                   LocalDate hireDate,
                   double salary,
                   String gender,
                   int childCount) {
        super(id,
              lastName, firstName, secondName,
              birthDate,
              hireDate, specName, salary,
              gender, childCount);
    }
}
