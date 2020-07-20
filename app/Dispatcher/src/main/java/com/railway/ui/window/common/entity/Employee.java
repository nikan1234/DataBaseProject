package com.railway.ui.window.common.entity;

import java.time.LocalDate;

public class Employee {
    public static final String[] genders = {"Male", "Female"};

    private int id;
    private String lastName;
    private String firstName;
    private String secondName;
    private LocalDate birthDate;
    private LocalDate hireDate;
    private double salary;
    private String gender;
    private int childCount;
    private String speciality;

    public Employee(int id,
                    String lastName,
                    String firstName,
                    String secondName,
                    LocalDate birthDate,
                    LocalDate hireDate,
                    String speciality,
                    double salary,
                    String gender,
                    int childCount) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.speciality = speciality;
        this.salary = salary;
        this.childCount = childCount;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Employee))
            return false;
        Employee other = (Employee) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        String format = "%s %s %s - %s";
        return String.format(format,
                lastName, firstName, secondName, speciality);
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getChildCount() {
        return childCount;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getSpeciality() {
        return speciality;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = birthDate;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
