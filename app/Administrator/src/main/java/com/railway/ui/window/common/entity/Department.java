package com.railway.ui.window.common.entity;


public class Department {
    private int departmentId;
    private String departmentName;
    private Manager manager;

    public Department(int id, String name, Manager manager) {
        this.departmentId = id;
        this.departmentName = name;
        this.manager = manager;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Manager getManager() {
        return manager;
    }
}
