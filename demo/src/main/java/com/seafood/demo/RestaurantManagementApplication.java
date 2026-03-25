package com.seafood.demo;

import com.seafood.demo.entity.Role;
import com.seafood.demo.entity.User;
import com.seafood.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository) {
		return args -> {
			// Kiểm tra xem đã có tài khoản admin chưa
			if (userRepository.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword("admin"); // Trong thực tế nên mã hóa
				admin.setFullName("Quản trị viên hệ thống");
				admin.setRole(Role.ADMIN);
				userRepository.save(admin);
				System.out.println("Đã khởi tạo tài khoản admin mặc định (admin/admin).");
			}
		};
	}
}
