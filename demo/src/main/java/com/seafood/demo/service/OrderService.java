package com.seafood.demo.service;

import com.seafood.demo.entity.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getRecentOrders();
    Optional<Order> getOrderById(Long id);
    Order saveOrder(Order order);
    void completeOrder(Long orderId);
    void deleteOrder(Long id);
}
