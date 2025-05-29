package io.messagequeue.server.dto.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.messagequeue.server.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> orderItems = new ArrayList<>();
}
