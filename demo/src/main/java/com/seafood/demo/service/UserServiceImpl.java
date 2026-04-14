package com.seafood.demo.service;

import com.seafood.demo.entity.User;
import com.seafood.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Lớp implementation thực hiện logic cụ thể của UserService.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password) {
        // Tìm user theo username trong database
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Lưu ý bảo mật: Trong thực tế dự án, cần dùng bcrypt để mã hóa hash mật khẩu thay vì chuỗi thô.
            // Ở đây đối soát mật khẩu nhập vào có giống với mật khẩu trong DB không.
            if (user.getPassword().equals(password)) {
                return user; // Mật khẩu đúng
            }
        }
        return null; // Sai mật khẩu hoặc tài khoản không tồn tại
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(com.seafood.demo.entity.Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user); // Thêm mới user vào CSDL
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Không cho phép đổi Username vì nó có thể là khóa duy nhất để tra cứu
            // Update các phần tử thông tin khác:
            user.setPassword(userDetails.getPassword());
            user.setFullName(userDetails.getFullName());
            user.setRole(userDetails.getRole());
            user.setPhone(userDetails.getPhone());
            user.setAddress(userDetails.getAddress());
            user.setPosition(userDetails.getPosition());
            return userRepository.save(user); // Lưu lại bản ghi đã update
        }
        return null; // Trả về null nếu không tìm thấy (Cũng có thể Custom Exception ở đây)
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
