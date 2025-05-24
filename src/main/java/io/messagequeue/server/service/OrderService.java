package io.messagequeue.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.messagequeue.server.dto.OrderDTO;
import io.messagequeue.server.messaging.OrderProducer;
import io.messagequeue.server.model.Order;
import io.messagequeue.server.model.OrderItem;
import io.messagequeue.server.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public OrderDTO createOrder(Order order) {
        // Gán lại quan hệ ngược
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
            }
        }

        // Lưu DB
        Order savedOrder = orderRepository.save(order);

        // Gửi message vào MQ
        return orderProducer.placeOrder(savedOrder);
    }
}
