package com.seafood.demo.controller;

import com.seafood.demo.entity.User;
import com.seafood.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // Có thể thêm kiểm tra session ở đây nếu muốn bảo mật các trang
        return "index"; // Trả về template index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Trả về template login.html
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        User user = userService.authenticate(username, password);
        if (user != null) {
            // Lưu thông tin đăng nhập vào session
            session.setAttribute("loggedInUser", user);
            session.setAttribute("user", user.getUsername());
            session.setAttribute("role", user.getRole().name());
            return "redirect:/"; // Đăng nhập thành công, chuyển hướng về trang chủ
        } else {
            // Thiết lập thông báo lỗi
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "login"; // Đăng nhập thất bại, quay lại trang đăng nhập
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
