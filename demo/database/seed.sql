-- =============================================
-- HỆ THỐNG QUẢN LÝ NHÀ HÀNG HẢI SẢN - Seed Data
-- =============================================

-- Dữ liệu mẫu cho bảng users
INSERT INTO users (username, password, full_name, role, status) VALUES
('admin', 'admin', 'Quản trị viên hệ thống', 'ADMIN', TRUE),
('nhanvien1', '123456', 'Nguyễn Văn A', 'EMPLOYEE', TRUE),
('thungan1', '123456', 'Trần Thị B', 'ACCOUNTANT', TRUE);

-- Dữ liệu mẫu cho bảng employees
INSERT INTO employees (full_name, phone, position, hire_date, status) VALUES
('Nguyễn Văn A', '0901234567', 'Nhân viên phục vụ', '2024-01-15', 'ACTIVE'),
('Trần Thị B', '0912345678', 'Thu ngân', '2024-02-01', 'ACTIVE'),
('Lê Văn C', '0923456789', 'Đầu bếp', '2024-03-10', 'ACTIVE');

-- Dữ liệu mẫu cho bảng categories
INSERT INTO categories (name, description) VALUES
('Hải sản nướng', 'Các món hải sản chế biến bằng phương pháp nướng'),
('Hải sản hấp', 'Các món hải sản chế biến bằng phương pháp hấp'),
('Hải sản sống', 'Các món hải sản tươi sống - sashimi'),
('Lẩu', 'Các loại lẩu hải sản'),
('Đồ uống', 'Các loại nước uống, nước ngọt, bia');

-- Dữ liệu mẫu cho bảng dishes
INSERT INTO dishes (name, price, category_id, description, status) VALUES
('Tôm hùm nướng bơ tỏi', 850000, 1, 'Tôm hùm tươi nướng bơ tỏi thơm ngon', TRUE),
('Cua rang muối', 450000, 1, 'Cua biển rang muối giòn rụm', TRUE),
('Mực hấp gừng', 280000, 2, 'Mực ống hấp gừng sả', TRUE),
('Nghêu hấp xả', 120000, 2, 'Nghêu hấp xả ớt', TRUE),
('Sashimi cá hồi', 350000, 3, 'Cá hồi tươi cắt lát mỏng', TRUE),
('Lẩu hải sản', 550000, 4, 'Lẩu hải sản thập cẩm', TRUE),
('Bia Tiger', 25000, 5, 'Bia Tiger lon 330ml', TRUE),
('Nước suối', 10000, 5, 'Nước suối Aquafina 500ml', TRUE);

-- Dữ liệu mẫu cho bảng tables
INSERT INTO tables (table_number, capacity, status) VALUES
('T01', 4, TRUE),
('T02', 4, TRUE),
('T03', 6, TRUE),
('T04', 6, TRUE),
('T05', 8, TRUE),
('T06', 10, TRUE);

-- Dữ liệu mẫu cho bảng customers
INSERT INTO customers (full_name, phone, email, loyalty_points) VALUES
('Phạm Văn D', '0934567890', 'phamd@email.com', 100),
('Hoàng Thị E', '0945678901', 'hoange@email.com', 50);
