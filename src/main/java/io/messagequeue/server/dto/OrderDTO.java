package io.messagequeue.server.dto;

import io.messagequeue.server.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Order order;
    private String orderStatus;
    private String message;
}
