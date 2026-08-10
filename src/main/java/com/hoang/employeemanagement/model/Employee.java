package com.hoang.employeemanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nhan_vien", unique = true, nullable = false, length = 50)
    @NotBlank(message = "Mã nhân viên không được để trống")
    @Size(min = 3, max = 50, message = "Mã nhân viên phải từ 3 đến 50 ký tự")
    private String maNhanVien;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(min = 2, max = 100, message = "Tên nhân viên phải từ 2 đến 100 ký tự")
    private String ten;

    @Column(name = "email", unique = true, length = 100)
    @Email(message = "Email không hợp lệ")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonBackReference
    private Department department;

    public Employee() {
    }

    public Employee(String maNhanVien, String ten) {
        this.maNhanVien = maNhanVien;
        this.ten = ten;
    }

    public Employee(String maNhanVien, String ten, String email, Department department) {
        this.maNhanVien = maNhanVien;
        this.ten = ten;
        this.email = email;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}