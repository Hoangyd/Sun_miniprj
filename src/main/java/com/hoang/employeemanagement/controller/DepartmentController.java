package com.hoang.employeemanagement.controller;

import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // GET /departments - Lấy tất cả phòng ban
    @GetMapping
    public ResponseEntity<List<Department>> layDanhSachDepartment() {
        List<Department> danhSach = departmentService.layDanhSachDepartment();
        return ResponseEntity.ok(danhSach);
    }

    // GET /departments/{id} - Lấy phòng ban theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Department> timDepartmentTheoId(@PathVariable Long id) {
        Optional<Department> department = departmentService.timDepartmentTheoId(id);
        return department.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /departments/name/{name} - Lấy phòng ban theo tên
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> timDepartmentTheoTen(@PathVariable String name) {
        Optional<Department> department = departmentService.timDepartmentTheoTen(name);
        return department.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /departments - Tạo mới phòng ban
    @PostMapping
    public ResponseEntity<Department> themDepartment(@RequestBody Department department) {
        Department newDept = departmentService.themDepartment(
                department.getName(),
                department.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newDept);
    }

    // PUT /departments/{id} - Cập nhật phòng ban
    @PutMapping("/{id}")
    public ResponseEntity<Department> capNhatDepartment(
            @PathVariable Long id,
            @RequestBody Department department) {

        Department updated = departmentService.capNhatDepartment(
                id,
                department.getName(),
                department.getDescription()
        );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }

    // DELETE /departments/{id} - Xóa phòng ban
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoaDepartment(@PathVariable Long id) {
        Optional<Department> department = departmentService.timDepartmentTheoId(id);
        if (department.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        departmentService.xoaDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
