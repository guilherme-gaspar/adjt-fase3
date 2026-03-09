package com.fiap.pedidoservice.infrastructure.mapper;

import com.fiap.pedidoservice.domain.model.Order;
import com.fiap.pedidoservice.domain.model.OrderItem;
import com.fiap.pedidoservice.domain.model.RestaurantData;
import com.fiap.pedidoservice.infrastructure.persistence.entity.OrderEntity;
import com.fiap.pedidoservice.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEntityMapper {

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setCustomerId(order.getCustomerId());
        entity.setRestaurantId(order.getRestaurantData().getRestaurantId());
        entity.setRestaurantName(order.getRestaurantData().getRestaurantName());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setStatus(order.getStatus());

        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(item -> {
                    OrderItemEntity itemEntity = new OrderItemEntity();
                    itemEntity.setProductId(item.getProductId());
                    itemEntity.setName(item.getName());
                    itemEntity.setQuantity(item.getQuantity());
                    itemEntity.setPrice(item.getPrice());
                    itemEntity.setOrder(entity);
                    return itemEntity;
                })
                .toList();

        entity.setItems(itemEntities);
        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        return new Order(
                entity.getId(),
                entity.getCustomerId(),
                new RestaurantData(entity.getRestaurantId(), entity.getRestaurantName()),
                entity.getItems().stream()
                        .map(item -> new OrderItem(
                                item.getProductId(),
                                item.getName(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList(),
                entity.getTotalAmount(),
                entity.getStatus()
        );
    }
}