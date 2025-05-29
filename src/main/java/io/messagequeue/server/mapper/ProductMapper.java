package io.messagequeue.server.mapper;

import org.mapstruct.Mapper;

import io.messagequeue.server.dto.order.ProductRequest;
import io.messagequeue.server.dto.order.ProductResponse;
import io.messagequeue.server.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper extends GenericMapper<ProductRequest, Product, ProductResponse>{

}
