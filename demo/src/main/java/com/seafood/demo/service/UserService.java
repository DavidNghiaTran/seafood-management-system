package com.seafood.demo.service;

import com.seafood.demo.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface cung cấp các định nghĩa hàm để Quản lý thông tin Người dùng / Nhân viên.
 */
public interface UserService {
    /**
     * Xác thực người dùng (Đăng nhập) dựa trên username và password.
     */
    User authenticate(String username, String password);

    /**
     * Lấy danh sách toàn bộ người dùng trong hệ thống.
     */
    List<User> getAllUsers();
    
    /**
     * Tìm danh sách người dùng dựa vào vai trò (Role: ADMIN hoặc EMPLOYEE).
     */
    List<User> getUsersByRole(com.seafood.demo.entity.Role role);
    
    /**
     * Lưu trữ một nhân viên / người dùng mới vào hệ thống.
     */
    User saveUser(User user);
    
    /**
     * Tìm kiếm thông tin người dùng theo ID.
     */
    Optional<User> getUserById(Long id);
    
    /**
     * Cập nhật thông tin của người dùng.
     */
    User updateUser(Long id, User userDetails);
    
    /**
     * Xóa tài khoản khỏi hệ thống.
     */
    void deleteUser(Long id);
}
