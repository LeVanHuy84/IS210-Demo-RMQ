package io.messagequeue.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.messagequeue.server.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
