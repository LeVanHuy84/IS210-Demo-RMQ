package io.messagequeue.server.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @NotNull(message="Product id is required")
    private Long productId;

    @NotNull(message="Quantity is required")
    private Integer quantity;
}
