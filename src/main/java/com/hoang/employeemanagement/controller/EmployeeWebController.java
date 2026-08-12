package com.hoang.employeemanagement.controller;

import com.hoang.employeemanagement.model.Employee;
import com.hoang.employeemanagement.model.Department;
import com.hoang.employeemanagement.dto.EmployeeStatistics;
import com.hoang.employeemanagement.service.EmployeeService;
import com.hoang.employeemanagement.service.DepartmentService;
import com.hoang.employeemanagement.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final ReportService reportService;

    public EmployeeWebController(EmployeeService employeeService, DepartmentService departmentService, ReportService reportService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.reportService = reportService;
    }

    // GET /employees/list - Hiển thị danh sách nhân viên
    @GetMapping("/list")
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.layDanhSachNhanVien();
        List<Department> departments = departmentService.layDanhSachDepartment();

        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);

        return "employees/list";
    }

    // GET /employees/add - Hiển thị form thêm nhân viên
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Department> departments = departmentService.layDanhSachDepartment();

        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departments);

        return "employees/add";
    }

    // POST /employees/add - Xử lý thêm nhân viên mới
    @PostMapping("/add")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long departmentId,
                              Model model) {

        if (bindingResult.hasErrors()) {
            List<Department> departments = departmentService.layDanhSachDepartment();
            model.addAttribute("departments", departments);
            return "employees/add";
        }

        // Thêm nhân viên vào DB
        employeeService.themNhanVien(
                employee.getTen(),
                employee.getEmail(),
                departmentId
        );

        return "redirect:/web/employees/list";
    }

    // GET /employees/search - Tìm kiếm nhân viên
    @GetMapping("/search")
    public String searchEmployees(
            @RequestParam(required = false) String ten,
            @RequestParam(required = false) Long departmentId,
            Model model) {

        List<Employee> employees = null;
        List<Department> departments = departmentService.layDanhSachDepartment();

        if (ten != null && !ten.isEmpty() && departmentId != null) {
            // Tìm theo tên + phòng ban
            employees = employeeService.timNhanVienTheoTenVaDepartment(ten, departmentId);
        } else if (ten != null && !ten.isEmpty()) {
            // Tìm theo tên
            employees = employeeService.timNhanVienTheoTen(ten);
        } else if (departmentId != null) {
            // Tìm theo phòng ban
            employees = employeeService.timNhanVienTheoDepartment(departmentId);
        } else {
            // Nếu không có filter, lấy tất cả
            employees = employeeService.layDanhSachNhanVien();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("searchTen", ten);
        model.addAttribute("searchDepartmentId", departmentId);

        return "employees/search";
    }

    // GET /employees/view/{id} - Xem chi tiết nhân viên
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.timNhanVienTheoId(id);

        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employees/view";
        }

        return "redirect:/web/employees/list";
    }

    // GET /employees/edit/{id} - Form chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Employee> employee = employeeService.timNhanVienTheoId(id);

        if (employee.isPresent()) {
            List<Department> departments = departmentService.layDanhSachDepartment();

            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", departments);

            return "employees/edit";
        }

        return "redirect:/web/employees/list";
    }

    // POST /employees/edit/{id} - Xử lý cập nhật nhân viên
    @PostMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id,
                               @Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long departmentId,
                               Model model) {

        if (bindingResult.hasErrors()) {
            List<Department> departments = departmentService.layDanhSachDepartment();
            model.addAttribute("departments", departments);
            return "employees/edit";
        }

        // Cập nhật nhân viên
        employeeService.capNhatNhanVien(id, employee.getTen(), employee.getEmail(), departmentId);

        return "redirect:/web/employees/list";
    }

    // GET /employees/delete/{id} - Xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.xoaNhanVien(id);
        return "redirect:/web/employees/list";
    }

    // GET /employees/statistics - Hiển thị thống kê nhân viên
    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        EmployeeStatistics stats = reportService.getEmployeeStatistics();
        model.addAttribute("stats", stats);
        return "employees/statistics";
    }
}
