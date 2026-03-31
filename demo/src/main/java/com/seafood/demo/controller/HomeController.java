package com.seafood.demo.controller;

import com.seafood.demo.entity.*;
import com.seafood.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        List<Order> allOrders = orderService.getAllOrders();
        LocalDate today = LocalDate.now();
        
        // 1. Doanh thu hôm nay
        BigDecimal todayRevenue = allOrders.stream()
                .filter(o -> o.getOrderDate().toLocalDate().isEqual(today))
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 2. Đơn hàng mới (hôm nay)
        long newOrdersCount = allOrders.stream()
                .filter(o -> o.getOrderDate().toLocalDate().isEqual(today))
                .count();

        // 3. Bàn đang phục vụ
        List<RestaurantTable> allTables = tableService.getAllTables();
        long occupiedTables = allTables.stream().filter(t -> !t.isStatus()).count();
        int totalTables = allTables.size();
        
        // 4. Tổng Khách Hàng
        int totalCustomers = customerService.getAllCustomers().size();
        
        // 5. Đơn hàng gần đây
        List<Order> recentOrders = orderService.getRecentOrders().stream()
                .limit(5)
                .collect(Collectors.toList());
                
        // 6. Món bán chạy
        Map<Dish, Integer> dishSales = new HashMap<>();
        for (Order order : allOrders) {
            if ("COMPLETED".equals(order.getStatus())) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    dishSales.put(detail.getDish(), dishSales.getOrDefault(detail.getDish(), 0) + detail.getQuantity());
                }
            }
        }
        
        List<Map.Entry<Dish, Integer>> topDishes = dishSales.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .collect(Collectors.toList());
                
        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("newOrdersCount", newOrdersCount);
        model.addAttribute("occupiedTables", occupiedTables);
        model.addAttribute("totalTables", totalTables);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("topDishes", topDishes);

        return "index";
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
