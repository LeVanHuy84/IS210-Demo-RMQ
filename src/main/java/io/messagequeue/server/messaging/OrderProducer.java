package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.order.OrderDTO;
import io.messagequeue.server.dto.order.OrderRequest;

@Service
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderDTO placeOrder(OrderRequest request, Long uid) {
        OrderDTO orderDTO = new OrderDTO(request, uid,"Pending" , "Đơn hàng đã được gửi và đang chờ xử lý");

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY , orderDTO);
        return orderDTO;
    }
}
