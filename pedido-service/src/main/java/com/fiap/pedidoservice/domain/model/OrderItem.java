package com.fiap.pedidoservice.domain.model;

import java.math.BigDecimal;

public class OrderItem {

    private Long productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;

    public OrderItem(Long productId, String name, Integer quantity, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}