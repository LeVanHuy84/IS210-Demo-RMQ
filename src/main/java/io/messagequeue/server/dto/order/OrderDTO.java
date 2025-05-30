package io.messagequeue.server.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private OrderRequest orderRequest;
    private Long uid;
    private String orderStatus;
    private String message;
}
