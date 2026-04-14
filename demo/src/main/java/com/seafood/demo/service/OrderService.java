package com.seafood.demo.service;

import com.seafood.demo.entity.Order;
import java.util.List;
import java.util.Optional;

/**
 * Interface định nghĩa các nghiệp vụ cốt lõi xử lý Đơn hàng (Order).
 */
public interface OrderService {
    /**
     * Lấy danh sách toàn bộ đơn hàng trong hệ thống
     */
    List<Order> getAllOrders();
    
    /**
     * Lấy danh sách đơn hàng sắp xếp theo thời gian mới nhất
     */
    List<Order> getRecentOrders();
    
    /**
     * Tìm kiếm đơn hàng theo ID
     */
    Optional<Order> getOrderById(Long id);
    
    /**
     * Lưu thông tin đơn hàng mới hoặc cập nhật đơn hàng
     */
    Order saveOrder(Order order);
    
    /**
     * Đánh dấu hoàn tất đơn hàng và giải phóng bàn
     */
    void completeOrder(Long orderId);
    
    /**
     * Xóa đơn hàng khỏi hệ thống
     */
    void deleteOrder(Long id);
}
