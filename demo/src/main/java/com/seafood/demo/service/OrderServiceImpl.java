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
        Order savedOrder = orderRepository.save(order);
        
        // Update table status
        if (savedOrder.getRestaurantTable() != null) {
            RestaurantTable table = savedOrder.getRestaurantTable();
            table.setStatus(false);
            tableRepository.save(table);
        }
        
        return savedOrder;
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus("COMPLETED");
            
            // Free the table
            if (order.getRestaurantTable() != null) {
                RestaurantTable table = order.getRestaurantTable();
                table.setStatus(true); // true = AVAILABLE
                tableRepository.save(table);
            }
            
            orderRepository.save(order);
        });
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
