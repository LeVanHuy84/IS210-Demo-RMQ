package io.messagequeue.server.dto.order;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull
    private BigDecimal price;
    private Integer quantity;
    private String imgUrl;
}
