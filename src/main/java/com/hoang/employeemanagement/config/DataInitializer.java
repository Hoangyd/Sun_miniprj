package com.hoang.employeemanagement.config;

import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.model.Employee;
import com.hoang.employeemanagement.repository.DepartmentRepository;
import com.hoang.employeemanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(DepartmentRepository departmentRepository,
                                   EmployeeRepository employeeRepository) {
        return args -> {
            // Kiểm tra nếu đã có dữ liệu trong database
            if (departmentRepository.count() > 0) {
                return;
            }

            // Tạo các phòng ban
            Department dept1 = new Department("IT", "Information Technology");
            Department dept2 = new Department("HR", "Human Resources");
            Department dept3 = new Department("Sales", "Sales Department");

            Department savedDept1 = departmentRepository.save(dept1);
            Department savedDept2 = departmentRepository.save(dept2);
            Department savedDept3 = departmentRepository.save(dept3);

            // Tạo các nhân viên
            Employee emp1 = new Employee("EMP-001", "Nguyễn Văn Hoàng", "hoang.nguyen@company.com", savedDept1);
            Employee emp2 = new Employee("EMP-002", "Trần Văn Nam", "nam.tran@company.com", savedDept1);
            Employee emp3 = new Employee("EMP-003", "Lê Thị Hương", "huong.le@company.com", savedDept2);
            Employee emp4 = new Employee("EMP-004", "Phạm Minh Tuấn", "tuan.pham@company.com", savedDept3);

            employeeRepository.save(emp1);
            employeeRepository.save(emp2);
            employeeRepository.save(emp3);
            employeeRepository.save(emp4);

            System.out.println("✓ Database initialized with sample data");
        };
    }
}
