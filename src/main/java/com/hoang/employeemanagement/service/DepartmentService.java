package com.hoang.employeemanagement.service;

import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> layDanhSachDepartment() {
        return departmentRepository.findAll();
    }

    public Optional<Department> timDepartmentTheoId(Long id) {
        return departmentRepository.findById(id);
    }

    public Optional<Department> timDepartmentTheoTen(String name) {
        return departmentRepository.findByName(name);
    }

    public Department themDepartment(String name, String description) {
        Department department = new Department(name, description);
        return departmentRepository.save(department);
    }

    public Department capNhatDepartment(Long id, String name, String description) {
        Optional<Department> deptOpt = departmentRepository.findById(id);

        if (deptOpt.isPresent()) {
            Department department = deptOpt.get();
            if (name != null && !name.isBlank()) {
                department.setName(name);
            }
            if (description != null && !description.isBlank()) {
                department.setDescription(description);
            }
            return departmentRepository.save(department);
        }

        return null;
    }

    public void xoaDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
