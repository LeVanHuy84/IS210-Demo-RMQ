package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.order.OrderDTO;
import io.messagequeue.server.model.Order;
import io.messagequeue.server.model.enums.OrderStatus;
import io.messagequeue.server.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(OrderDTO orderDTO){
        System.out.println("Consumer is able to consume message form queues"+orderDTO);

        // Cập nhật trạng thái đơn hàng trong DB (nếu cần)
        Order order = orderRepository.findById(orderDTO.getOrderResponse().getId()).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.PROCESSING); // ví dụ enum
            orderRepository.save(order);
        }
    }
}
