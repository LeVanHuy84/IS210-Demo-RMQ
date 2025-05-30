package io.messagequeue.server.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import io.messagequeue.server.config.RabbitMQConfig;
import io.messagequeue.server.dto.order.OrderDTO;
import io.messagequeue.server.service.OrderService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    private final OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void consume(OrderDTO orderDTO){
        try {
            System.out.println("Consumer is able to consume message form queues"+orderDTO);
            // Bước xử lý đơn hàng (giả lập):
            System.out.println("Kiểm tra tồn kho...");
            System.out.println("Xác nhận thanh toán...");
            System.out.println("Tạo đơn hàng và lưu vào DB...");

            // Cập nhật trạng thái đơn hàng trong DB (nếu cần)
            orderService.createOrder(orderDTO.getOrderRequest(), orderDTO.getUid());
        } catch (Exception e) {
            throw new RuntimeException("Error");
        }
    }
}
