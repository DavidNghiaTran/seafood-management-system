# HỆ THỐNG WEB QUẢN LÝ NHÀ HÀNG HẢI SẢN

## 1. Giới thiệu dự án

Hệ thống Web Quản Lý Nhà Hàng Hải Sản là một ứng dụng hỗ trợ quản lý toàn bộ hoạt động vận hành trong nhà hàng, bao gồm quản lý tài khoản, thực đơn, bàn ăn, khách hàng, đơn hàng, hóa đơn, nhân viên, chấm công và thống kê doanh thu.

Dự án được xây dựng nhằm giải quyết các vấn đề thường gặp trong quá trình vận hành nhà hàng như:
- Quản lý món ăn và danh mục món còn thủ công
- Khó theo dõi trạng thái bàn theo thời gian thực
- Dễ sai sót khi tạo đơn hàng và thanh toán
- Khó kiểm soát lịch sử hóa đơn và doanh thu
- Việc quản lý nhân viên và chấm công chưa tập trung

Hệ thống hướng đến việc:
- Tự động hóa quy trình quản lý nhà hàng
- Giảm sai sót trong quá trình phục vụ và thanh toán
- Tăng tốc độ thao tác của nhân viên
- Hỗ trợ quản lý theo dõi hoạt động kinh doanh hiệu quả hơn
- Tạo nền tảng dữ liệu để mở rộng hệ thống về sau

---

## 2. Mục tiêu của đề tài

Dự án được thực hiện nhằm đáp ứng yêu cầu của bài tập lớn môn học, đồng thời rèn luyện các kỹ năng:
- Phân tích và thiết kế hệ thống phần mềm
- Xây dựng ứng dụng theo hướng đối tượng (OOP)
- Thiết kế và thao tác với cơ sở dữ liệu quan hệ
- Phân chia module và làm việc nhóm
- Phát triển ứng dụng web có backend, frontend và database
- Quản lý mã nguồn bằng GitHub

Đề tài thuộc nhóm **ứng dụng quản lý thông thường, không kết hợp IoT**. Theo yêu cầu bài tập, nhóm 4 thành viên cần xây dựng tối thiểu **12 use case**, tương ứng với `4 × 3 use case`. Hệ thống hiện tại đã đáp ứng đủ yêu cầu đó.

---

## 3. Phạm vi hệ thống

Hệ thống hỗ trợ các chức năng chính sau:
- Đăng nhập hệ thống
- Quản lý tài khoản
- Quản lý danh mục món ăn
- Quản lý món ăn
- Quản lý bàn
- Quản lý khách hàng
- Tạo đơn hàng
- Thanh toán hóa đơn
- Xem lịch sử hóa đơn
- Quản lý nhân viên
- Chấm công nhân viên
- Thống kê doanh thu

Đây là một **web application**, dữ liệu được lưu trữ bằng **cơ sở dữ liệu quan hệ MySQL**.

---

## 4. Đối tượng sử dụng

Hệ thống được thiết kế cho các nhóm người dùng sau:

### 4.1 Admin
- Quản lý tài khoản hệ thống
- Quản lý danh mục món, món ăn, bàn
- Quản lý dữ liệu nhân viên

### 4.2 Nhân viên phục vụ
- Tiếp nhận khách
- Tạo đơn hàng
- Quản lý thông tin khách hàng
- Chấm công làm việc

### 4.3 Thu ngân
- Thực hiện thanh toán hóa đơn
- Cập nhật trạng thái đơn hàng sau thanh toán

### 4.4 Quản lý
- Theo dõi lịch sử hóa đơn
- Xem thống kê doanh thu
- Xem tình hình hoạt động nhà hàng

---

## 5. Danh sách Use Case

Hệ thống gồm 12 use case chính:

| Mã Use Case | Tên Use Case | Actor |
|------------|--------------|-------|
| UC01 | Đăng nhập hệ thống | Admin / Nhân viên |
| UC02 | Quản lý tài khoản | Admin |
| UC03 | Quản lý danh mục món | Admin |
| UC04 | Quản lý món ăn | Admin |
| UC05 | Quản lý bàn | Admin |
| UC06 | Quản lý khách hàng | Nhân viên |
| UC07 | Tạo đơn hàng | Nhân viên |
| UC08 | Thanh toán hóa đơn | Thu ngân |
| UC09 | Xem lịch sử hóa đơn | Quản lý |
| UC10 | Quản lý nhân viên | Admin |
| UC11 | Chấm công nhân viên | Nhân viên |
| UC12 | Thống kê doanh thu | Quản lý |

Tổng số use case: **12**

---

## 6. Mô tả chức năng chi tiết

### 6.1 Đăng nhập hệ thống
Chức năng cho phép người dùng truy cập hệ thống bằng tài khoản đã được cấp.

**Các xử lý chính:**
- Nhập tên đăng nhập
- Nhập mật khẩu
- Kiểm tra thông tin hợp lệ
- Phân quyền theo vai trò
- Chuyển đến giao diện chính

**Ý nghĩa:**
- Bảo mật hệ thống
- Kiểm soát quyền truy cập theo từng vai trò

---

### 6.2 Quản lý tài khoản
Admin có quyền quản lý toàn bộ tài khoản đăng nhập trong hệ thống.

**Các chức năng:**
- Thêm tài khoản mới
- Cập nhật thông tin tài khoản
- Xóa tài khoản
- Phân quyền người dùng

**Dữ liệu quản lý:**
- Username
- Password
- Vai trò
- Trạng thái tài khoản

---

### 6.3 Quản lý danh mục món
Quản lý các nhóm món ăn trong thực đơn để thuận tiện cho việc tổ chức dữ liệu.

**Các chức năng:**
- Thêm danh mục
- Sửa danh mục
- Xóa danh mục
- Xem danh sách danh mục

**Ví dụ danh mục:**
- Hải sản nướng
- Hải sản hấp
- Hải sản sống
- Lẩu
- Đồ uống

---

### 6.4 Quản lý món ăn
Cho phép quản lý thông tin chi tiết của từng món trong thực đơn.

**Các chức năng:**
- Thêm món ăn
- Sửa thông tin món
- Xóa món ăn
- Xem danh sách món

**Thông tin món ăn gồm:**
- Tên món
- Giá bán
- Danh mục
- Mô tả
- Trạng thái (còn bán / ngừng bán)

---

### 6.5 Quản lý bàn
Quản lý thông tin bàn ăn trong nhà hàng.

**Các chức năng:**
- Thêm bàn
- Cập nhật thông tin bàn
- Xóa bàn
- Xem trạng thái bàn

**Thông tin bàn gồm:**
- Số bàn
- Sức chứa
- Trạng thái bàn:
  - Trống
  - Đang phục vụ

---

### 6.6 Quản lý khách hàng
Lưu trữ và cập nhật thông tin khách hàng phục vụ cho việc tạo đơn và chăm sóc khách hàng.

**Các chức năng:**
- Thêm khách hàng
- Cập nhật thông tin khách hàng
- Tìm kiếm khách hàng
- Xem danh sách khách hàng

**Thông tin quản lý gồm:**
- Tên khách hàng
- Số điện thoại
- Email
- Điểm tích lũy

---

### 6.7 Tạo đơn hàng
Nhân viên phục vụ sử dụng chức năng này để tạo đơn cho khách tại bàn.

**Quy trình xử lý:**
1. Chọn bàn
2. Chọn món ăn
3. Nhập số lượng
4. Hệ thống tính tổng tiền
5. Lưu đơn hàng vào cơ sở dữ liệu

**Mục tiêu:**
- Giảm sai sót khi ghi món
- Tăng tốc độ phục vụ
- Đồng bộ dữ liệu với hóa đơn

---

### 6.8 Thanh toán hóa đơn
Thu ngân thực hiện thanh toán cho các đơn hàng đã hoàn thành.

**Quy trình xử lý:**
1. Chọn đơn hàng cần thanh toán
2. Hiển thị tổng tiền
3. Xác nhận thanh toán
4. Cập nhật trạng thái đơn hàng
5. Cập nhật bàn về trạng thái trống

**Kết quả:**
- Sinh hóa đơn thanh toán
- Ghi nhận doanh thu
- Kết thúc một vòng đời đơn hàng

---

### 6.9 Xem lịch sử hóa đơn
Quản lý có thể xem lại các hóa đơn đã thanh toán để phục vụ tra cứu và kiểm tra.

**Các chức năng:**
- Xem hóa đơn theo ngày
- Xem hóa đơn theo nhân viên
- Xem theo khoảng thời gian

**Ý nghĩa:**
- Hỗ trợ đối soát
- Kiểm tra lịch sử giao dịch
- Phục vụ thống kê, báo cáo

---

### 6.10 Quản lý nhân viên
Cho phép quản lý danh sách nhân sự trong nhà hàng.

**Các chức năng:**
- Thêm nhân viên
- Sửa thông tin nhân viên
- Xóa nhân viên
- Xem danh sách nhân viên

**Thông tin quản lý có thể gồm:**
- Mã nhân viên
- Họ tên
- Số điện thoại
- Chức vụ
- Ngày vào làm
- Trạng thái làm việc

---

### 6.11 Chấm công nhân viên
Ghi nhận thời gian làm việc của nhân viên theo ngày.

**Các chức năng:**
- Check-in
- Check-out
- Xem lịch sử chấm công

**Thông tin lưu trữ:**
- Ngày làm việc
- Giờ vào
- Giờ ra
- Tổng số giờ làm

**Ý nghĩa:**
- Hỗ trợ quản lý nhân sự
- Làm cơ sở tính công hoặc đối chiếu thời gian làm việc

---

### 6.12 Thống kê doanh thu
Quản lý xem tổng quan hoạt động kinh doanh của nhà hàng.

**Các chức năng:**
- Thống kê theo ngày
- Thống kê theo tháng
- Xem món bán chạy

**Thông tin hiển thị:**
- Tổng doanh thu
- Tổng số đơn hàng
- Danh sách món bán chạy

**Ý nghĩa:**
- Giúp nhà quản lý đưa ra quyết định
- Theo dõi hiệu quả kinh doanh
- Phân tích xu hướng tiêu dùng

---

## 7. Kiến trúc hệ thống

Hệ thống được xây dựng theo mô hình web application gồm 3 phần chính:

### 7.1 Frontend
- Xây dựng giao diện người dùng
- Hiển thị dữ liệu
- Gửi yêu cầu đến backend
- Nhận phản hồi và hiển thị kết quả

**Công nghệ sử dụng:**
- HTML
- CSS
- JavaScript

### 7.2 Backend
- Xử lý logic nghiệp vụ
- Xác thực người dùng
- Kiểm tra dữ liệu đầu vào
- Kết nối cơ sở dữ liệu
- Quản lý API và request/response

**Công nghệ sử dụng:**
- Java
- Spring Boot
- Spring Data JPA
- MVC Pattern

### 7.3 Database
- Lưu trữ dữ liệu toàn hệ thống
- Đảm bảo tính nhất quán dữ liệu
- Phục vụ truy vấn nghiệp vụ và báo cáo

**Công nghệ sử dụng:**
- MySQL

---

## 8. Công nghệ sử dụng

### 8.1 Backend
- **Java**: xây dựng logic nghiệp vụ, xử lý chức năng hệ thống
- **Spring Boot**: phát triển ứng dụng web và API
- **Spring Data JPA**: hỗ trợ thao tác với cơ sở dữ liệu
- **Mô hình MVC**: tổ chức mã nguồn rõ ràng, dễ bảo trì

### 8.2 Frontend
- **HTML**: xây dựng cấu trúc giao diện
- **CSS**: thiết kế bố cục, màu sắc, trình bày
- **JavaScript**: xử lý tương tác và cập nhật dữ liệu

### 8.3 Cơ sở dữ liệu
- **MySQL**: lưu trữ dữ liệu quan hệ
- Công cụ quản lý:
  - MySQL Workbench
  - DBeaver

### 8.4 Công cụ phát triển
- **Visual Studio Code**
- Extension Pack for Java
- Spring Boot Extension Pack
- GitLens

### 8.5 Quản lý mã nguồn
- **Git**
- **GitHub**

### 8.6 Triển khai
- **Render** để deploy hệ thống web

---

## 9. Thiết kế cơ sở dữ liệu

Hệ thống đề xuất các bảng chính như sau:

1. `users`
2. `employees`
3. `attendance`
4. `customers`
5. `tables`
6. `categories`
7. `dishes`
8. `orders`
9. `order_details`
10. `invoices`

### 9.1 Mô tả vai trò các bảng

#### `users`
Lưu tài khoản đăng nhập hệ thống.
- id
- username
- password
- role
- status

#### `employees`
Lưu thông tin nhân viên.
- id
- full_name
- phone
- position
- hire_date
- status

#### `attendance`
Lưu dữ liệu chấm công.
- id
- employee_id
- work_date
- check_in
- check_out
- total_hours

#### `customers`
Lưu thông tin khách hàng.
- id
- full_name
- phone
- email
- loyalty_points

#### `tables`
Lưu thông tin bàn ăn.
- id
- table_number
- capacity
- status

#### `categories`
Lưu danh mục món ăn.
- id
- category_name
- description

#### `dishes`
Lưu thông tin món ăn.
- id
- dish_name
- price
- category_id
- description
- status

#### `orders`
Lưu thông tin đơn hàng.
- id
- customer_id
- table_id
- employee_id
- order_time
- total_amount
- status

#### `order_details`
Lưu chi tiết từng món trong đơn hàng.
- id
- order_id
- dish_id
- quantity
- unit_price
- subtotal

#### `invoices`
Lưu thông tin hóa đơn thanh toán.
- id
- order_id
- payment_time
- total_amount
- payment_method
- cashier_id

---

## 10. Quan hệ giữa các bảng

- Một `category` có nhiều `dishes`
- Một `customer` có thể có nhiều `orders`
- Một `table` có thể phục vụ nhiều `orders` theo thời gian
- Một `order` có nhiều `order_details`
- Một `dish` có thể xuất hiện trong nhiều `order_details`
- Một `order` sau khi thanh toán sẽ tạo `invoice`
- Một `employee` có thể tạo nhiều `orders`
- Một `employee` có thể có nhiều bản ghi `attendance`
- Một `user` có thể liên kết với một `employee` tùy cách thiết kế hệ thống

---

## 11. Áp dụng lập trình hướng đối tượng (OOP)

Dự án đáp ứng yêu cầu bắt buộc về OOP thông qua việc xây dựng các lớp đối tượng đại diện cho các thực thể trong hệ thống.

### 11.1 Các lớp đối tượng chính
- `User`
- `Employee`
- `Attendance`
- `Customer`
- `Table`
- `Category`
- `Dish`
- `Order`
- `OrderDetail`
- `Invoice`

### 11.2 Các nguyên lý OOP có thể áp dụng
- **Encapsulation**: đóng gói dữ liệu bằng thuộc tính private và getter/setter
- **Abstraction**: tách biệt logic nghiệp vụ qua service/repository/controller
- **Inheritance**: có thể mở rộng từ lớp người dùng chung nếu cần
- **Polymorphism**: áp dụng trong xử lý vai trò hoặc nghiệp vụ mở rộng

### 11.3 Phân lớp trong dự án
- `entity`: ánh xạ dữ liệu database
- `repository`: thao tác dữ liệu
- `service`: xử lý nghiệp vụ
- `controller`: tiếp nhận request từ frontend
- `dto`: truyền dữ liệu giữa các tầng
- `config`: cấu hình hệ thống

---

## 12. Cấu trúc thư mục gợi ý

```bash
restaurant-management/
│
├── src/
│   ├── main/
│   │   ├── java/com/restaurant/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── config/
│   │   │   └── RestaurantManagementApplication.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       ├── application.properties
│   │       └── data.sql
│   │
│   └── test/
│       └── java/com/restaurant/
│
├── database/
│   ├── schema.sql
│   └── seed.sql
│
├── docs/
│   ├── use-case-diagram.png
│   ├── erd.png
│   └── report.pdf
│
├── README.md
├── pom.xml
└── .gitignore