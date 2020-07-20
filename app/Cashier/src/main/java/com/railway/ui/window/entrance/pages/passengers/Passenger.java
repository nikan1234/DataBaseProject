package com.railway.ui.window.entrance.pages.passengers;

public class Passenger {
    public static final String[] genders = {"Male", "Female"};

    private String lastName;
    private String firstName;
    private String secondName;
    private int age;
    private String gender;

    public Passenger(String lastName,
                     String firstName,
                     String secondName,
                     int age,
                     String gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.gender = gender;
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

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }
}
