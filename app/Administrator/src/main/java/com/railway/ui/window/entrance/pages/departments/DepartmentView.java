package com.railway.ui.window.entrance.pages.departments;

import com.railway.ui.window.common.entity.Department;
import com.railway.ui.window.common.entity.Manager;


public class DepartmentView {
    private Department department;
    private int departmentId;
    private String departmentName;
    private String managerLastName = "";
    private String managerFirstName = "";
    private String managerSecondName = "";

    public DepartmentView(Department department) {
        this.department = department;

        departmentId = department.getDepartmentId();
        departmentName = department.getDepartmentName();

        Manager manager = department.getManager();
        if (manager == null)
            return;
        managerLastName = manager.getLastName();
        managerFirstName = manager.getFirstName();
        managerSecondName = manager.getSecondName();
    }

    public Department getDepartment() {
        return department;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String getManagerLastName() {
        return managerLastName;
    }

    public String getManagerFirstName() {
        return managerFirstName;
    }

    public String getManagerSecondName() {
        return managerSecondName;
    }
}
