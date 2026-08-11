package com.hoang.employeemanagement.service;

import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

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
        logger.info("Creating new department: name={}, description={}", name, description);
        Department department = new Department(name, description);
        Department savedDept = departmentRepository.save(department);
        logger.info("Department created successfully: id={}, name={}", savedDept.getId(), savedDept.getName());
        return savedDept;
    }

    public Department capNhatDepartment(Long id, String name, String description) {
        logger.info("Updating department: id={}, name={}, description={}", id, name, description);
        Optional<Department> deptOpt = departmentRepository.findById(id);

        if (deptOpt.isPresent()) {
            Department department = deptOpt.get();
            if (name != null && !name.isBlank()) {
                department.setName(name);
            }
            if (description != null && !description.isBlank()) {
                department.setDescription(description);
            }
            Department updatedDept = departmentRepository.save(department);
            logger.info("Department updated successfully: id={}, name={}", updatedDept.getId(), updatedDept.getName());
            return updatedDept;
        }

        logger.warn("Department not found for update: id={}", id);
        return null;
    }

    public void xoaDepartment(Long id) {
        logger.info("Deleting department: id={}", id);
        departmentRepository.deleteById(id);
        logger.info("Department deleted successfully: id={}", id);
    }
}
