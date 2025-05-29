package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.order.OrderDTO;
import io.messagequeue.server.dto.order.OrderResponse;

@Service
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderDTO placeOrder(OrderResponse response) {
        OrderDTO orderDTO = new OrderDTO(response,"Order Placed" , "Hi Producer Your Order is Placed ");

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY , orderDTO);
        return orderDTO;
    }
}
