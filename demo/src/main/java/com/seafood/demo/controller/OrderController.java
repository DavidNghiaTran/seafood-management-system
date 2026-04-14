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

/**
 * Controller xử lý các yêu cầu liên quan đến Đơn hàng (Order).
 * Nơi tiếp nhận các endpoint như xem danh sách, tạo mới, và thanh toán hóa đơn.
 */
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

    /**
     * Hiển thị danh sách các đơn hàng gần đây (Hóa Đơn).
     * @param model Đối tượng để truyền dữ liệu từ Controller sang View.
     * @return Tên của template view (orders/list.html).
     */
    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getRecentOrders());
        return "orders/list";
    }

    /**
     * Hiển thị giao diện POS (Point of Sale) để tạo đơn hàng mới.
     * Load các danh sách cần thiết như: Bàn trống, Khách hàng, Thực đơn.
     */
    @GetMapping("/create")
    public String showCreateOrderForm(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("dishes", dishService.getAllDishes());
        return "orders/create";
    }

    /**
     * Xử lý luồng tạo đơn hàng mới khi người dùng submit form từ giao diện POS.
     * 
     * @param tableId ID bàn được chọn
     * @param customerId ID khách hàng (có thể null nếu là khách lẻ)
     * @param note Ghi chú thêm cho đơn hàng
     * @param params Map chứa các ID món ăn và số lượng được chọn
     * @param session HTTP Session để lấy thông tin nhân viên (User) đang lập đơn
     * @param redirectAttributes Dùng để truyền câu thông báo thành công/lỗi sau khi chuyển hướng
     * @return Đường dẫn chuyển hướng (redirect) sau khi xử lý.
     */
    @PostMapping("/save")
    public String saveOrder(@RequestParam("tableId") Long tableId,
                            @RequestParam(value = "customerId", required = false) Long customerId,
                            @RequestParam(value = "note", required = false) String note,
                            @RequestParam Map<String, String> params,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
                            
        // 1. Kiểm tra quyền và lấy thông tin nhân viên lập đơn
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Nếu chưa đăng nhập thì chuyển về trang login
        }

        // 2. Khởi tạo Đơn hàng mới
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now()); // Thời gian tạo đơn
        order.setStatus("PENDING"); // Đơn hàng mới tạo luôn ở trạng thái PENDING (chưa thanh toán)
        order.setCreatedBy(loggedInUser); // Người lập đơn

        // Cập nhật Ghi chú (nếu có)
        if (note != null && !note.trim().isEmpty()) {
            order.setNote(note.trim());
        }

        // 3. Gán Bàn vào hệ thống đơn hàng
        tableService.getTableById(tableId).ifPresent(order::setRestaurantTable);

        // 4. Gán thông tin Khách hàng (nếu là khách quen)
        if (customerId != null) {
            customerService.getCustomerById(customerId).ifPresent(order::setCustomer);
        }

        List<OrderDetail> details = new ArrayList<>();
        // 5. Trích xuất thông tin món ăn và số lượng từ dữ liệu submit lên (params)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            // Định dạng thẻ input món ăn là 'dish_{id}'
            if (entry.getKey().startsWith("dish_")) {
                Long dishId = Long.parseLong(entry.getKey().substring(5)); // Lấy ID món
                Integer quantity = Integer.parseInt(entry.getValue()); // Lấy số lượng đặt
                
                // Chỉ xử lý những món có số lượng > 0
                if (quantity > 0) {
                    dishService.getDishById(dishId).ifPresent(dish -> {
                        // Chỉ cho phép đặt các món hiện đang còn phục vụ (status = true)
                        if (dish.isStatus()) {
                            OrderDetail detail = new OrderDetail();
                            detail.setDish(dish);
                            detail.setQuantity(quantity);
                            detail.setPrice(BigDecimal.valueOf(dish.getPrice())); // Lấy giá hiện tại của món
                            details.add(detail);
                        }
                    });
                }
            }
        }

        // Kiểm tra xem đơn hàng có món ăn nào không, nếu không báo lỗi
        if (details.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ít nhất 1 món ăn!");
            return "redirect:/orders/create";
        }

        // 6. Gán Chi tiết đơn hàng và lưu vào cơ sở dữ liệu
        order.setOrderDetails(details);
        orderService.saveOrder(order);

        redirectAttributes.addFlashAttribute("successMessage", "Tạo đơn hàng thành công!");
        return "redirect:/orders";
    }

    /**
     * Xử lý cập nhật trạng thái đơn hàng thành hoàn tất (Thanh toán).
     * 
     * @param id ID của đơn hàng cần thanh toán
     */
    @GetMapping("/complete/{id}")
    public String completeOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        orderService.completeOrder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã thanh toán hóa đơn!");
        return "redirect:/orders";
    }
}
