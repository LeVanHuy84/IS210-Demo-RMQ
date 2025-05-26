package io.messagequeue.server.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.messagequeue.server.dto.OrderDTO;
import io.messagequeue.server.dto.OrderItemRequest;
import io.messagequeue.server.dto.OrderRequest;
import io.messagequeue.server.dto.OrderResponse;
import io.messagequeue.server.mapper.OrderMapper;
import io.messagequeue.server.messaging.OrderProducer;
import io.messagequeue.server.model.Order;
import io.messagequeue.server.model.OrderItem;
import io.messagequeue.server.model.Product;
import io.messagequeue.server.model.enums.OrderStatus;
import io.messagequeue.server.repository.OrderRepository;
import io.messagequeue.server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProducer orderProducer;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public List<OrderResponse> getAllOrders() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }

    public OrderDTO createOrder(OrderRequest request) {
        Order order = orderMapper.toEntity(request);
        // Gán lại quan hệ ngược
        List<Long> productIds = request.getOrderItems().stream().map(OrderItemRequest::getProductId).toList();
            Map<Long, Product> products = productRepository
                    .findAllById(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, item -> item));
            List<OrderItem> orderItems = request.getOrderItems().stream()
                    .map(item -> {
                        Product product = products.get(item.getProductId());
                        return OrderItem.builder()
                                .product(product)
                                .quantity(item.getQuantity())
                                .order(order)
                                .build();
                    }).toList();
            order.setOrderItems(orderItems);

        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);

        // Lưu DB
        Order savedOrder = orderRepository.save(order);
        // Chuyển đổi sang DTO
        OrderResponse orderResponse = orderMapper.toDTO(savedOrder);

        // Gửi message vào MQ
        return orderProducer.placeOrder(orderResponse);
    }
}




    // public OrderDTO createOrder(OrderRequest request) {
    //     Order order = orderMapper.toEntity(request);
    //     // Gán lại quan hệ ngược
    //     List<Long> productIds = request.getOrderItems().stream().map(OrderItemRequest::getProductId).toList();
    //         Map<Long, Product> products = productRepository
    //                 .findAllById(productIds).stream()
    //                 .collect(Collectors.toMap(Product::getId, item -> item));
    //         List<OrderItem> orderItems = request.getOrderItems().stream()
    //                 .map(item -> {
    //                     Product product = products.get(item.getProductId());
    //                     return OrderItem.builder()
    //                             .product(product)
    //                             .quantity(item.getQuantity())
    //                             .order(order)
    //                             .build();
    //                 }).toList();
    //         order.setOrderItems(orderItems);

    //     order.setOrderItems(orderItems);
    //     order.setStatus(OrderStatus.PENDING);

    //     // Lưu DB
    //     Order savedOrder = orderRepository.save(order);
    //     // Chuyển đổi sang DTO
    //     OrderResponse orderResponse = orderMapper.toDTO(savedOrder);

    //     // Gửi message vào MQ
    //     return orderProducer.placeOrder(orderResponse);
    // }