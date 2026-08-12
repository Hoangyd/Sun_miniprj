package com.hoang.employeemanagement.dto;

import java.util.List;

public class EmployeeStatistics {
    private Long totalEmployees;
    private List<DepartmentStatistics> departmentStatistics;

    public EmployeeStatistics() {
    }

    public EmployeeStatistics(Long totalEmployees, List<DepartmentStatistics> departmentStatistics) {
        this.totalEmployees = totalEmployees;
        this.departmentStatistics = departmentStatistics;
    }

    public Long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(Long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public List<DepartmentStatistics> getDepartmentStatistics() {
        return departmentStatistics;
    }

    public void setDepartmentStatistics(List<DepartmentStatistics> departmentStatistics) {
        this.departmentStatistics = departmentStatistics;
    }
}
