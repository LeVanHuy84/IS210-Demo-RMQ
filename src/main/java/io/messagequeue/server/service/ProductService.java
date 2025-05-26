package io.messagequeue.server.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import io.messagequeue.server.dto.ProductRequest;
import io.messagequeue.server.dto.ProductResponse;
import io.messagequeue.server.mapper.ProductMapper;
import io.messagequeue.server.model.Product;
import io.messagequeue.server.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse create(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    public List<ProductResponse> findAll() {
        return productMapper.toDTOs(productRepository.findAll());
    }

    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
        return productMapper.toDTO(product);
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

        // Cập nhật thông tin
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImgUrl(request.getImgUrl());

        Product updated = productRepository.save(product);
        return productMapper.toDTO(updated);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}
