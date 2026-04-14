package com.seafood.demo.controller;

import com.seafood.demo.entity.Order;
import com.seafood.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller xử lý các chức năng báo cáo và thống kê (Dành cho Quản trị viên/Chủ nhà hàng).
 * Cung cấp dữ liệu để vẽ biểu đồ và phân tích tình hình kinh doanh.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private OrderService orderService;

    /**
     * Hiển thị giao diện báo cáo doanh thu tổng quan.
     * Thống kê theo ngày và theo tháng từ tất cả các đơn hàng đã thanh toán.
     */
    @GetMapping
    public String showRevenueReport(Model model) {
        // 1. Lọc và lấy tất cả các đơn hàng đã được đánh dấu là HOÀN THÀNH (COMPLETED)
        List<Order> completedOrders = orderService.getAllOrders().stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .collect(Collectors.toList());

        // 2. Tính Tổng doanh thu toàn phần (tổng tiền của tất cả đơn hàng)
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Nhóm doanh thu lại theo Tháng (định dạng yyyy-MM) để thể hiện biểu đồ xu hướng tháng
        Map<String, BigDecimal> revenueByMonth = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
                ));

        // 4. Nhóm doanh thu lại theo Ngày (định dạng yyyy-MM-dd) phục vụ biểu đồ chi tiết ngày
        Map<String, BigDecimal> revenueByDay = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
                ));
                
        // 5. Cấu trúc lại Map theo dạng TreeMap và sắp xếp giảm dần (để hiển thị các ngày/tháng gần nhất lên trước)
        TreeMap<String, BigDecimal> sortedRevenueByMonth = new TreeMap<>(Collections.reverseOrder());
        sortedRevenueByMonth.putAll(revenueByMonth);

        TreeMap<String, BigDecimal> sortedRevenueByDay = new TreeMap<>(Collections.reverseOrder());
        sortedRevenueByDay.putAll(revenueByDay);

        // 6. Đưa dữ liệu sang View (Giao diện) để render trang
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrdersCount", completedOrders.size()); // Tổng số hoá đơn
        model.addAttribute("revenueByMonth", sortedRevenueByMonth);
        model.addAttribute("revenueByDay", sortedRevenueByDay);

        return "reports/index";
    }
}
