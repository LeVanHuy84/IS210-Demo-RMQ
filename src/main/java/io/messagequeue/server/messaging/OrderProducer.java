package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.OrderDTO;
import io.messagequeue.server.model.Order;

@Service
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public OrderDTO placeOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO(order,"Order Placed" , "Hi Producer Your Order is Placed ");

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY , orderDTO);
        return orderDTO;
    }
}
