-- Cập nhật cột role trong bảng users để chấp nhận các giá trị mới
ALTER TABLE users MODIFY COLUMN role VARCHAR(255) NOT NULL;
