package com.seafood.demo.service;

import com.seafood.demo.entity.Order;
import com.seafood.demo.entity.OrderDetail;
import com.seafood.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getRecentOrders() {
        return orderRepository.findByOrderByOrderDateDesc();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order saveOrder(Order order) {
        // Calculate total amount if order details are present
        if (order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()) {
            BigDecimal total = order.getOrderDetails().stream()
                .map(detail -> detail.getPrice().multiply(new BigDecimal(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setTotalAmount(total);

            // Maintain bidirectional relationship
            for (OrderDetail detail : order.getOrderDetails()) {
                detail.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
