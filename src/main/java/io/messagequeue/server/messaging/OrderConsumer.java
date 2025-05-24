package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.OrderDTO;

@Component
public class OrderConsumer {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(OrderDTO orderDTO){
        System.out.println("Consumer is able to consume messgae form queues"+orderDTO);
    }
}
