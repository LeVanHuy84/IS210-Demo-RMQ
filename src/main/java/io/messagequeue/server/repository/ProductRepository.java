package io.messagequeue.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.messagequeue.server.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
