package io.messagequeue.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.messagequeue.server.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
