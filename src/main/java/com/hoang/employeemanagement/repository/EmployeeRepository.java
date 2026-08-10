package com.hoang.employeemanagement.repository;

import com.hoang.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByMaNhanVien(String maNhanVien);

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.ten) LIKE LOWER(CONCAT('%', :ten, '%'))")
    List<Employee> findByTenContainingIgnoreCase(@Param("ten") String ten);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.ten) LIKE LOWER(CONCAT('%', :ten, '%')) AND e.department.id = :departmentId")
    List<Employee> findByTenAndDepartment(@Param("ten") String ten, @Param("departmentId") Long departmentId);
}
