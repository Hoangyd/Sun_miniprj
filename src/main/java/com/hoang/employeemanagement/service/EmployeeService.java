package com.hoang.employeemanagement.service;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final UtilityService utilityService;

    public EmployeeService(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    public String taoThongTinNhanVien(String ten, int soThuTu) {

        String tenDaFormat = utilityService.vietHoaTen(ten);

        String maNhanVien = utilityService.taoMaNhanVien(soThuTu);

        return "Ma nhan vien: " + maNhanVien
                + " | Ten: " + tenDaFormat;
    }
}