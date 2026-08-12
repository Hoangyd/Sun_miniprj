package com.hoang.employeemanagement.service;

import com.hoang.employeemanagement.dto.DepartmentStatistics;
import com.hoang.employeemanagement.dto.EmployeeStatistics;
import com.hoang.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public ReportService(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @Cacheable(value = "employeeStatistics", cacheManager = "cacheManager")
    public EmployeeStatistics getEmployeeStatistics() {
        logger.info("Generating employee statistics report");

        long totalEmployees = employeeService.layTongSoNhanVien();
        List<DepartmentStatistics> departmentStats = employeeRepository.getEmployeeCountByDepartment();

        EmployeeStatistics stats = new EmployeeStatistics(totalEmployees, departmentStats);
        logger.info("Employee statistics report generated: total={}, departments={}", totalEmployees, departmentStats.size());
        return stats;
    }

    public List<DepartmentStatistics> getDepartmentStatistics() {
        logger.info("Fetching department statistics");
        List<DepartmentStatistics> stats = employeeRepository.getEmployeeCountByDepartment();
        logger.info("Department statistics fetched: count={}", stats.size());
        return stats;
    }

    public long getTotalEmployeeCount() {
        logger.info("Fetching total employee count");
        return employeeService.layTongSoNhanVien();
    }
}
