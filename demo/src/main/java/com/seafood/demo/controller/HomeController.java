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

        // 1. Dùng vòng lặp for cơ bản để tính doanh thu và đếm đơn hôm nay
        BigDecimal todayRevenue = BigDecimal.ZERO;
        long newOrdersCount = 0;

        for (Order order : allOrders) {
            // Nếu ngày tạo đơn hàng là hôm nay
            if (order.getOrderDate().toLocalDate().isEqual(today)) {
                newOrdersCount++; // Đếm thêm 1 đơn mới
                
                // Nếu đơn hàng đã hoàn thành thì cộng dồn vào doanh thu
                if ("COMPLETED".equals(order.getStatus())) {
                    todayRevenue = todayRevenue.add(order.getTotalAmount());
                }
            }
        }

        // 2. Dùng vòng lặp để đếm số bàn đang bận
        List<RestaurantTable> allTables = tableService.getAllTables();
        long occupiedTables = 0;
        for (RestaurantTable table : allTables) {
            if (!table.isStatus()) {
                occupiedTables++;
            }
        }
        int totalTables = allTables.size();
        int totalCustomers = customerService.getAllCustomers().size();

        // 3. Lấy 5 đơn hàng gần nhất bằng vòng lặp thay vì dùng .limit(5)
        List<Order> allRecentOrders = orderService.getRecentOrders();
        List<Order> recentOrders = new ArrayList<>();
        for (int i = 0; i < allRecentOrders.size(); i++) {
            if (i >= 5) break; // Chỉ lấy tối đa 5 đơn
            recentOrders.add(allRecentOrders.get(i));
        }

        // 4. Thống kê số lượng từng món ăn (Dùng if/else cơ bản thay cho getOrDefault)
        Map<Dish, Integer> dishSales = new HashMap<>();
        for (Order order : allOrders) {
            if ("COMPLETED".equals(order.getStatus())) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    Dish dish = detail.getDish();
                    int currentQuantity = 0;
                    
                    // Kiểm tra xem món này đã có trong Map chưa
                    if (dishSales.containsKey(dish)) {
                        currentQuantity = dishSales.get(dish);
                    }
                    
                    // Cập nhật lại số lượng
                    dishSales.put(dish, currentQuantity + detail.getQuantity());
                }
            }
        }

        // 5. Sắp xếp Map để lấy 5 món bán chạy nhất bằng Collections.sort
        // Đưa các phần tử của Map vào một List để sắp xếp
        List<Map.Entry<Dish, Integer>> listDishes = new ArrayList<>(dishSales.entrySet());
        
        Collections.sort(listDishes, new Comparator<Map.Entry<Dish, Integer>>() {
            @Override
            public int compare(Map.Entry<Dish, Integer> item1, Map.Entry<Dish, Integer> item2) {
                // Sắp xếp giảm dần theo số lượng (value)
                return item2.getValue().compareTo(item1.getValue()); 
            }
        });

        // Chỉ lấy 5 món đứng đầu sau khi đã sắp xếp
        List<Map.Entry<Dish, Integer>> topDishes = new ArrayList<>();
        for (int i = 0; i < listDishes.size(); i++) {
            if (i >= 5) break;
            topDishes.add(listDishes.get(i));
        }

        // Đưa dữ liệu sang view
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
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {
        User user = userService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("loggedInUser", user);
            session.setAttribute("user", user.getUsername());
            session.setAttribute("role", user.getRole().name());
            return "redirect:/";
        } else {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
