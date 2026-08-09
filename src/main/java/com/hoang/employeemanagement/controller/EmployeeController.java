package com.hoang.employeemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoang.employeemanagement.service.EmployeeService;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public String taoNhanVien(
            @RequestParam String ten,
            @RequestParam int soThuTu) {

        return employeeService.taoThongTinNhanVien(ten, soThuTu);
    }
}