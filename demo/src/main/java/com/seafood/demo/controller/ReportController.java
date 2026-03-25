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

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showRevenueReport(Model model) {
        // Lấy tất cả các hóa đơn đã hoàn thành
        List<Order> completedOrders = orderService.getAllOrders().stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .collect(Collectors.toList());

        // Tổng doanh thu toàn phần
        BigDecimal totalRevenue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Nhóm doanh thu theo tháng (yyyy-MM)
        Map<String, BigDecimal> revenueByMonth = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
                ));

        // Nhóm doanh thu theo ngày (yyyy-MM-dd)
        Map<String, BigDecimal> revenueByDay = completedOrders.stream()
                .collect(Collectors.groupingBy(
                        o -> o.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
                ));
                
        // Sắp xếp giảm dần theo thời gian (để hiện ngày gần nhất trước)
        TreeMap<String, BigDecimal> sortedRevenueByMonth = new TreeMap<>(Collections.reverseOrder());
        sortedRevenueByMonth.putAll(revenueByMonth);

        TreeMap<String, BigDecimal> sortedRevenueByDay = new TreeMap<>(Collections.reverseOrder());
        sortedRevenueByDay.putAll(revenueByDay);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrdersCount", completedOrders.size());
        model.addAttribute("revenueByMonth", sortedRevenueByMonth);
        model.addAttribute("revenueByDay", sortedRevenueByDay);

        return "reports/index";
    }
}
