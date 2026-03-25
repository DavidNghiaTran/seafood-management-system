-- Dữ liệu khởi tạo mặc định khi ứng dụng Spring Boot chạy lần đầu
-- Spring Boot sẽ tự động load file này nếu đặt spring.sql.init.mode=always

-- Cập nhật cột role trong bảng users để chấp nhận các giá trị mới
ALTER TABLE users MODIFY COLUMN role VARCHAR(255) NOT NULL;
