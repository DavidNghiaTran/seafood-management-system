package com.seafood.demo.controller;

import com.seafood.demo.entity.*;
import com.seafood.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private TableService tableService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private DishService dishService;

    // View all orders (Hóa Đơn)
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getRecentOrders());
        return "orders/list";
    }

    // View POS layout (Tạo Đơn Hàng)
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("dishes", dishService.getAllDishes());
        return "orders/create";
    }

    // Process order creation via form submission
    @PostMapping("/save")
    public String saveOrder(@RequestParam("tableId") Long tableId,
                            @RequestParam(value = "customerId", required = false) Long customerId,
                            @RequestParam Map<String, String> params,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
                            
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setCreatedBy(loggedInUser);

        // Assign Table
        tableService.getTableById(tableId).ifPresent(order::setRestaurantTable);

        // Assign Customer (optional)
        if (customerId != null) {
            customerService.getCustomerById(customerId).ifPresent(order::setCustomer);
        }

        List<OrderDetail> details = new ArrayList<>();
        // Extract dish quantities from params
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey().startsWith("dish_")) {
                Long dishId = Long.parseLong(entry.getKey().substring(5));
                Integer quantity = Integer.parseInt(entry.getValue());
                
                if (quantity > 0) {
                    dishService.getDishById(dishId).ifPresent(dish -> {
                        OrderDetail detail = new OrderDetail();
                        detail.setDish(dish);
                        detail.setQuantity(quantity);
                        detail.setPrice(BigDecimal.valueOf(dish.getPrice()));
                        details.add(detail);
                    });
                }
            }
        }

        if (details.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ít nhất 1 món ăn!");
            return "redirect:/orders/create";
        }

        order.setOrderDetails(details);
        orderService.saveOrder(order);
        
        // Update table status
        RestaurantTable table = order.getRestaurantTable();
        if (table != null) {
            table.setStatus(false); // false = OCCUPIED
            tableService.saveTable(table);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Tạo đơn hàng thành công!");
        return "redirect:/orders";
    }

    // Mark as completed
    @GetMapping("/complete/{id}")
    public String completeOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        orderService.getOrderById(id).ifPresent(order -> {
            order.setStatus("COMPLETED");
            orderService.saveOrder(order);
            
            // Free the table
            RestaurantTable table = order.getRestaurantTable();
            if (table != null) {
                table.setStatus(true); // true = AVAILABLE
                tableService.saveTable(table);
            }
        });
        redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn!");
        return "redirect:/orders";
    }
}
