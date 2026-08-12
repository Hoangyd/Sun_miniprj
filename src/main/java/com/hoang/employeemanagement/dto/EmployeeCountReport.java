package com.hoang.employeemanagement.dto;

public class EmployeeCountReport {
    private long totalEmployees;

    public EmployeeCountReport(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }
}
