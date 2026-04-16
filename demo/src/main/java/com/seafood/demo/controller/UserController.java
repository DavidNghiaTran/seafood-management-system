package com.seafood.demo.controller;

import com.seafood.demo.entity.Role;
import com.seafood.demo.entity.User;
import com.seafood.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller xử lý các nghiệp vụ liên quan đến Quản lý Nhân sự / Người dùng hệ thống.
 * Cung cấp các thao tác CRUD: Xem danh sách, Thêm, Sửa, Xóa nhân viên.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Hiển thị danh sách toàn bộ người dùng (nhân viên) trong hệ thống.
     */
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    /**
     * Hiển thị form thêm mới nhân viên.
     * Cung cấp danh sách các Role (vai trò) để gán cho nhân viên mới.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "users/form";
    }

    /**
     * Nhận dữ liệu submit từ form và lưu thông tin nhân viên mới vào cơ sở dữ liệu.
     */
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, Model model, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("errorMessage", "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác!");
            return "users/form";
        }
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thêm mới tài khoản thành công!");
        return "redirect:/users";
    }

    /**
     * Hiển thị form chỉnh sửa thông tin cho một nhân viên cụ thể đã có trong hệ thống.
     * 
     * @param id Mã ID của nhân viên cần chỉnh sửa
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("roles", Role.values());
            return "users/form";
        }
        return "redirect:/users";
    }

    /**
     * Xử lý luồng lưu trữ cập nhật thông tin nhân viên sau khi sửa form.
     * 
     * @param id ID nhân viên đang được cập nhật
     * @param user Đối tượng chứa thông tin mới do người dùng nhập vào
     */
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userService.updateUser(id, user);
        return "redirect:/users";
    }

    /**
     * Xóa một nhân viên khỏi hệ thống dựa vào ID và trả về trang danh sách.
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
