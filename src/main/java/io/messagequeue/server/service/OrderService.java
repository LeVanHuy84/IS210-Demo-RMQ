package io.messagequeue.server.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

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

    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Gán lại quan hệ ngược
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }

        // Lưu DB
        Order savedOrder = orderRepository.save(order);

        // Gửi message vào MQ
        orderProducer.placeOrder(savedOrder);

        return savedOrder;
    }
}
