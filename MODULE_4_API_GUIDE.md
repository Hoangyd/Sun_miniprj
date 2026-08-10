# Module 4: Spring Boot + Database (Spring Data JPA)

## 📋 Nội dung Module 4

✅ Cấu hình MySQL Database  
✅ Entity + Repository (JpaRepository)  
✅ CRUD Operations với Database  
✅ Tạo bảng employee (id, name, email, department_id)  
✅ Tạo bảng department  
✅ Relationship giữa Employee và Department  
✅ Tích hợp CRUD Employee với Database  
✅ Chức năng tìm kiếm theo tên hoặc phòng ban  

## 🔧 Cấu hình Database

**MySQL Connection:**
- Host: localhost
- Port: 3306
- Database: employee_db
- Username: root
- Password: 123456

**Hibernate Auto DDL:** `update` - tự động tạo/cập nhật bảng từ Entity

## 📁 Cấu trúc Project

```
src/main/java/com/hoang/employeemanagement/
├── model/
│   ├── Employee.java          (JPA Entity)
│   └── Department.java        (JPA Entity)
├── repository/
│   ├── EmployeeRepository.java   (JpaRepository)
│   └── DepartmentRepository.java (JpaRepository)
├── service/
│   ├── EmployeeService.java   (Business logic)
│   └── DepartmentService.java
├── controller/
│   ├── EmployeeController.java
│   └── DepartmentController.java
└── config/
    └── DataInitializer.java   (Sample data)
```

## 🔗 Database Schema

### Bảng: employees
```sql
CREATE TABLE employees (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  ma_nhan_vien VARCHAR(50) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE,
  department_id BIGINT,
  FOREIGN KEY (department_id) REFERENCES departments(id)
);
```

### Bảng: departments
```sql
CREATE TABLE departments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255)
);
```

## 📡 API Endpoints

### Employee Endpoints

#### 1. Lấy tất cả nhân viên
```
GET /employees
```
**Response:**
```json
[
  {
    "id": 1,
    "maNhanVien": "EMP-001",
    "ten": "Nguyễn Văn Hoàng",
    "email": "hoang@company.com",
    "department": {
      "id": 1,
      "name": "IT",
      "description": "Information Technology"
    }
  }
]
```

#### 2. Lấy nhân viên theo ID
```
GET /employees/{id}
```
**Example:** `GET /employees/1`

#### 3. Lấy nhân viên theo mã nhân viên
```
GET /employees/ma/{maNhanVien}
```
**Example:** `GET /employees/ma/EMP-001`

#### 4. Tìm kiếm nhân viên theo tên
```
GET /employees/search?ten=hoang
```

#### 5. Tìm kiếm nhân viên theo phòng ban
```
GET /employees/department/{departmentId}
```
**Example:** `GET /employees/department/1`

#### 6. Tìm kiếm nâng cao (tên + phòng ban)
```
GET /employees/search-advanced?ten=hoang&departmentId=1
GET /employees/search-advanced?ten=hoang
GET /employees/search-advanced?departmentId=1
```

#### 7. Tạo mới nhân viên
```
POST /employees
Content-Type: application/json

{
  "ten": "Phạm Minh Đức",
  "email": "duc.pham@company.com",
  "department": {
    "id": 1
  }
}
```

#### 8. Cập nhật nhân viên
```
PUT /employees/{id}
Content-Type: application/json

{
  "ten": "Phạm Minh Đức Updated",
  "email": "duc.updated@company.com",
  "department": {
    "id": 2
  }
}
```

#### 9. Xóa nhân viên
```
DELETE /employees/{id}
```
**Example:** `DELETE /employees/1`

---

### Department Endpoints

#### 1. Lấy tất cả phòng ban
```
GET /departments
```

#### 2. Lấy phòng ban theo ID
```
GET /departments/{id}
```

#### 3. Lấy phòng ban theo tên
```
GET /departments/name/{name}
```
**Example:** `GET /departments/name/IT`

#### 4. Tạo mới phòng ban
```
POST /departments
Content-Type: application/json

{
  "name": "Finance",
  "description": "Finance Department"
}
```

#### 5. Cập nhật phòng ban
```
PUT /departments/{id}
Content-Type: application/json

{
  "name": "Finance Updated",
  "description": "Updated description"
}
```

#### 6. Xóa phòng ban
```
DELETE /departments/{id}
```

---

## 🎯 Tính năng Modules 1-3 được giữ lại

✅ **Module 1 (Hello API):**
- GET /hello - Xin chào từ server

✅ **Module 2 (Business Logic):**
- Utility functions: vietHoaTen, taoMaNhanVien
- Tìm kiếm nhân viên theo tên
- Thêm nhân viên mới

✅ **Module 3 (Enhanced Features):**
- Các endpoint đã cải tiến với Database integration
- Tìm kiếm nâng cao

✅ **Module 4 (NEW - Database):**
- ✨ JPA Entity Mapping
- ✨ Repository pattern
- ✨ Full CRUD operations
- ✨ Advanced search (name + department)
- ✨ Department management
- ✨ Database persistence

## 🚀 Cách chạy

1. **Đảm bảo MySQL đang chạy:**
```bash
mysql -u root -p
```

2. **Tạo database:**
```sql
CREATE DATABASE employee_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Chạy application:**
```bash
mvn spring-boot:run
```

4. **Application sẽ tự động:**
   - ✅ Tạo tables (employees, departments)
   - ✅ Load sample data

## 📝 Ghi chú quan trọng

### Relationship: Employee ↔ Department
- **Type:** Many-to-One
- **Employee.department:** @ManyToOne, @JoinColumn(name="department_id")
- **Department.employees:** @OneToMany(mappedBy="department", cascade=CascadeType.ALL)

### Custom Queries
Repository có các custom queries:
- `findByMaNhanVien()` - Find by employee code
- `findByTenContainingIgnoreCase()` - Search by name (case-insensitive)
- `findByDepartmentId()` - Find by department
- `findByTenAndDepartment()` - Advanced search

### Error Handling
- 404 Not Found - Khi resource không tồn tại
- 201 Created - Khi tạo resource thành công
- 204 No Content - Khi delete thành công

## 📊 Flow Diagram

```
Request → Controller → Service → Repository → Database
   ↓        (Route)    (Logic)   (Data Access)   (Storage)
Response ← Entity ← Query Result ← SQL Operations
```

## ✅ Checklist Module 4

- [x] MySQL Database configuration
- [x] Employee Entity with JPA annotations
- [x] Department Entity with relationship
- [x] EmployeeRepository with custom queries
- [x] DepartmentRepository
- [x] EmployeeService with CRUD operations
- [x] DepartmentService
- [x] EmployeeController - Full REST API
- [x] DepartmentController - Full REST API
- [x] Advanced search functionality
- [x] Sample data initialization
- [x] Database auto-schema creation
- [x] All previous modules functionality preserved

---

**Module 4 Completed Successfully! 🎉**
