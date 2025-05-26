package io.messagequeue.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.messagequeue.server.dto.OrderDTO;
import io.messagequeue.server.dto.OrderRequest;
import io.messagequeue.server.dto.OrderResponse;
import io.messagequeue.server.service.OrderService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request) {
        OrderDTO savedOrder = orderService.createOrder(request);
        return ResponseEntity.ok(savedOrder);
    }
}
