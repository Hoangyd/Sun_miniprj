package com.hoang.employeemanagement.service;

import com.hoang.employeemanagement.model.Employee;
import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.repository.EmployeeRepository;
import com.hoang.employeemanagement.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository,
                          DepartmentRepository departmentRepository,
                          UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
    }

    public List<Employee> layDanhSachNhanVien() {
        return employeeRepository.findAll();
    }

    public Employee themNhanVien(String ten) {
        return themNhanVien(ten, null, null);
    }

    public Employee themNhanVien(String ten, String email, Long departmentId) {
        long count = employeeRepository.count();
        String maNhanVien = utilityService.taoMaNhanVien((int) (count + 1));

        Employee employee = new Employee();
        employee.setMaNhanVien(maNhanVien);
        employee.setTen(utilityService.vietHoaTen(ten));
        employee.setEmail(email);

        if (departmentId != null) {
            Optional<Department> department = departmentRepository.findById(departmentId);
            department.ifPresent(employee::setDepartment);
        }

        return employeeRepository.save(employee);
    }

    public Optional<Employee> timNhanVienTheoId(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee timNhanVienTheoMa(String maNhanVien) {
        return employeeRepository.findByMaNhanVien(maNhanVien).orElse(null);
    }

    public Optional<Employee> timNhanVienTheoEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public List<Employee> timNhanVienTheoTen(String ten) {
        return employeeRepository.findByTenContainingIgnoreCase(ten);
    }

    public List<Employee> timNhanVienTheoDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public List<Employee> timNhanVienTheoTenVaDepartment(String ten, Long departmentId) {
        return employeeRepository.findByTenAndDepartment(ten, departmentId);
    }

    public Employee capNhatNhanVien(Long id, String ten, String email, Long departmentId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            if (ten != null && !ten.isBlank()) {
                employee.setTen(utilityService.vietHoaTen(ten));
            }
            if (email != null && !email.isBlank()) {
                employee.setEmail(email);
            }
            if (departmentId != null) {
                Optional<Department> department = departmentRepository.findById(departmentId);
                department.ifPresent(employee::setDepartment);
            }
            return employeeRepository.save(employee);
        }

        return null;
    }

    public void xoaNhanVien(Long id) {
        employeeRepository.deleteById(id);
    }
}