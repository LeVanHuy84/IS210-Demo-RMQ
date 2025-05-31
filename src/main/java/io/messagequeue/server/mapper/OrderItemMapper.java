package io.messagequeue.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.messagequeue.server.dto.order.OrderItemRequest;
import io.messagequeue.server.dto.order.OrderItemResponse;
import io.messagequeue.server.model.OrderItem;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, 
    unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderItemMapper extends GenericMapper<OrderItemRequest, OrderItem, OrderItemResponse>{
    @Override
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imgUrl", target = "imgUrl")
    @Mapping(source = "product.price", target = "price")
    OrderItemResponse toDTO(OrderItem entity);
}
