package io.messagequeue.server.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.messagequeue.server.dto.order.OrderDTO;
import io.messagequeue.server.dto.order.OrderRequest;
import io.messagequeue.server.dto.order.OrderResponse;
import io.messagequeue.server.messaging.OrderProducer;
import io.messagequeue.server.service.OrderService;
import io.messagequeue.server.utils.AuthUtils;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderProducer orderProducer;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getAllOrdersForCurrentUser() {
        return ResponseEntity.ok(orderService.getAllForCurrentUser());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request) {
        Long uid = AuthUtils.getCurrentUserId();
        return ResponseEntity.ok(orderProducer.placeOrder(request, uid));
    }
}
