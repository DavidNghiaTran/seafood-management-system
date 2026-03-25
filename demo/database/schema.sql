-- =============================================
-- HỆ THỐNG QUẢN LÝ NHÀ HÀNG HẢI SẢN - Schema
-- =============================================

-- Bảng users: Lưu tài khoản đăng nhập hệ thống
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status BOOLEAN DEFAULT TRUE
);

-- Bảng employees: Lưu thông tin nhân viên
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    position VARCHAR(50),
    hire_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- Bảng attendance: Lưu dữ liệu chấm công
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    total_hours DECIMAL(5,2),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Bảng customers: Lưu thông tin khách hàng
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    loyalty_points INT DEFAULT 0
);

-- Bảng tables: Lưu thông tin bàn ăn
CREATE TABLE IF NOT EXISTS tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_number VARCHAR(10) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    status BOOLEAN DEFAULT TRUE
);

-- Bảng categories: Lưu danh mục món ăn
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

-- Bảng dishes: Lưu thông tin món ăn
CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    category_id BIGINT NOT NULL,
    description TEXT,
    status BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Bảng orders: Lưu thông tin đơn hàng
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT,
    table_id BIGINT,
    employee_id BIGINT,
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DOUBLE DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (table_id) REFERENCES tables(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id)
);

-- Bảng order_details: Lưu chi tiết từng món trong đơn hàng
CREATE TABLE IF NOT EXISTS order_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dishes(id)
);

-- Bảng invoices: Lưu thông tin hóa đơn thanh toán
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    payment_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DOUBLE NOT NULL,
    payment_method VARCHAR(20),
    cashier_id BIGINT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (cashier_id) REFERENCES users(id)
);
