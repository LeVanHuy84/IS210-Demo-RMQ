package io.messagequeue.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.messagequeue.server.dto.OrderRequest;
import io.messagequeue.server.dto.OrderResponse;
import io.messagequeue.server.model.Order;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderItemMapper.class})
public interface OrderMapper extends GenericMapper<OrderRequest, Order, OrderResponse>{

}
