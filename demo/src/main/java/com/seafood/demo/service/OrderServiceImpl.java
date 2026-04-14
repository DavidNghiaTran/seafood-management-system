package com.seafood.demo.service;

import com.seafood.demo.entity.Order;
import com.seafood.demo.entity.OrderDetail;
import com.seafood.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.seafood.demo.entity.RestaurantTable;
import com.seafood.demo.repository.TableRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implement các logic nghiệp vụ cho thực thể Đơn hàng (Order).
 * Bao gồm các thao tác tính toán tổng tiền, xử lý trạng thái bàn.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableRepository tableRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getRecentOrders() {
        // Lấy danh sách đơn hàng đã được sắp xếp giảm dần theo thời gian tạo
        return orderRepository.findByOrderByOrderDateDesc();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional // Đảm bảo tính toàn vẹn dữ liệu (rollback nếu có lỗi trong quá trình lưu)
    public Order saveOrder(Order order) {
        // 1. Nếu đơn hàng có danh sách món ăn, tiến hành tính tổng tiền (Total Amount)
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            BigDecimal total = order.getOrderDetails().stream()
                .map(detail -> detail.getPrice().multiply(new BigDecimal(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);

            // 2. Bảo trì mối quan hệ 2 chiều (Bidirectional Mapping) 
            // Gán ngược đơn hàng cha cho từng chi tiết món ăn (child)
            for (OrderDetail detail : order.getOrderDetails()) {
                detail.setOrder(order);
            }
        }
        
        // 3. Tiến hành lưu đơn hàng xuống Database (Cascade sẽ tự động lưu OrderDetails)
        Order savedOrder = orderRepository.save(order);
        
        // 4. Cập nhật lại Trạng thái của Bàn nhà hàng
        if (savedOrder.getRestaurantTable() != null) {
            RestaurantTable table = savedOrder.getRestaurantTable();
            // Đặt trạng thái bàn thành "Đang phục vụ" (false)
            table.setStatus(false);
            tableRepository.save(table);
        }
        
        return savedOrder;
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            // 1. Chuyển trạng thái hóa đơn sang đã thanh toán (COMPLETED)
            order.setStatus("COMPLETED");
            
            // 2. Giải phóng Bàn (Khi khách thanh toán xong thì bàn trống đón khách mới)
            if (order.getRestaurantTable() != null) {
                RestaurantTable table = order.getRestaurantTable();
                table.setStatus(true); // true = Bàn trống (AVAILABLE)
                tableRepository.save(table);
            }
            
            // 3. Lưu lại cập nhật
            orderRepository.save(order);
        });
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
