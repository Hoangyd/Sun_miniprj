package com.hoang.employeemanagement.controller;

import com.hoang.employeemanagement.model.Employee;
import com.hoang.employeemanagement.dto.EmployeeCountReport;
import com.hoang.employeemanagement.dto.EmployeeStatistics;
import com.hoang.employeemanagement.service.EmployeeService;
import com.hoang.employeemanagement.service.ReportService;
import com.hoang.employeemanagement.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ReportService reportService;

    public EmployeeController(EmployeeService employeeService, ReportService reportService) {
        this.employeeService = employeeService;
        this.reportService = reportService;
    }

    // GET /employees - Lấy tất cả nhân viên
    @GetMapping
    public ResponseEntity<List<Employee>> layDanhSachNhanVien() {
        List<Employee> danhSach = employeeService.layDanhSachNhanVien();
        return ResponseEntity.ok(danhSach);
    }

    // GET /employees/report/count - Báo cáo tổng số nhân viên (cached)
    @GetMapping("/report/count")
    public ResponseEntity<EmployeeCountReport> layTongSoNhanVien() {
        long count = employeeService.layTongSoNhanVien();
        return ResponseEntity.ok(new EmployeeCountReport(count));
    }

    // GET /employees/statistics - Thống kê nhân viên theo phòng ban (cached)
    @GetMapping("/statistics")
    public ResponseEntity<EmployeeStatistics> layThongKeNhanVien() {
        EmployeeStatistics stats = reportService.getEmployeeStatistics();
        return ResponseEntity.ok(stats);
    }

    // GET /employees/{id} - Lấy nhân viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> timNhanVienTheoId(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.timNhanVienTheoId(id);
        return employee.map(ResponseEntity::ok)
                       .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    // GET /employees/ma/{maNhanVien} - Lấy nhân viên theo mã nhân viên
    @GetMapping("/ma/{maNhanVien}")
    public ResponseEntity<Employee> timNhanVienTheoMa(@PathVariable String maNhanVien) {
        Employee employee = employeeService.timNhanVienTheoMa(maNhanVien);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    // GET /employees/search?ten=hoang - Tìm kiếm nhân viên theo tên
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> timTheoTen(@RequestParam String ten) {
        List<Employee> ketQua = employeeService.timNhanVienTheoTen(ten);
        return ResponseEntity.ok(ketQua);
    }

    // GET /employees/department/{departmentId} - Tìm nhân viên theo phòng ban
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Employee>> timTheoPhongBan(@PathVariable Long departmentId) {
        List<Employee> ketQua = employeeService.timNhanVienTheoDepartment(departmentId);
        return ResponseEntity.ok(ketQua);
    }

    // GET /employees/search-advanced?ten=hoang&departmentId=1 - Tìm kiếm nâng cao
    @GetMapping("/search-advanced")
    public ResponseEntity<List<Employee>> timTheoTenVaPhongBan(
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) Long departmentId) {

        if (ten != null && departmentId != null) {
            List<Employee> ketQua = employeeService.timNhanVienTheoTenVaDepartment(ten, departmentId);
            return ResponseEntity.ok(ketQua);
        } else if (ten != null) {
            List<Employee> ketQua = employeeService.timNhanVienTheoTen(ten);
            return ResponseEntity.ok(ketQua);
        } else if (departmentId != null) {
            List<Employee> ketQua = employeeService.timNhanVienTheoDepartment(departmentId);
            return ResponseEntity.ok(ketQua);
        }

        return ResponseEntity.ok(employeeService.layDanhSachNhanVien());
    }

    // POST /employees - Tạo mới nhân viên
    @PostMapping
    public ResponseEntity<Employee> themNhanVien(@Valid @RequestBody Employee employee) {
        Employee employeeMoi = employeeService.themNhanVien(
                employee.getTen(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeMoi);
    }

    // PUT /employees/{id} - Cập nhật nhân viên
    @PutMapping("/{id}")
    public ResponseEntity<Employee> capNhatNhanVien(
            @PathVariable Long id,
            @Valid @RequestBody Employee employee) {

        Employee updated = employeeService.capNhatNhanVien(
                id,
                employee.getTen(),
                employee.getEmail(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null
        );

        if (updated == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        return ResponseEntity.ok(updated);
    }

    // DELETE /employees/{id} - Xóa nhân viên
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoaNhanVien(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.timNhanVienTheoId(id);
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.xoaNhanVien(id);
        return ResponseEntity.noContent().build();
    }
}