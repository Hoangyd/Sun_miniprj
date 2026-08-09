package com.hoang.employeemanagement.service;

import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    // Viet hoa chu cai dau cua moi tu
    public String vietHoaTen(String ten) {

        if (ten == null || ten.isBlank()) {
            return ten;
        }

        String[] cacTu = ten.trim().split("\\s+");

        StringBuilder ketQua = new StringBuilder();

        for (String tu : cacTu) {

            String tuMoi = tu.substring(0, 1).toUpperCase()
                    + tu.substring(1).toLowerCase();

            ketQua.append(tuMoi).append(" ");
        }

        return ketQua.toString().trim();
    }

    // Tao ma nhan vien
    public String taoMaNhanVien(int soThuTu) {

        return String.format("EMP-%03d", soThuTu);
    }
}