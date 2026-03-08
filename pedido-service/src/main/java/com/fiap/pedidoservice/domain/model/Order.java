package com.fiap.pedidoservice.domain.model;

import com.fiap.pedidoservice.domain.exception.InvalidOrderStatusException;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private Long id;
    private Long customerId;
    private RestaurantData restaurantData;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;

    public Order(Long id,
                 Long customerId,
                 RestaurantData restaurantData,
                 List<OrderItem> items,
                 BigDecimal totalAmount,
                 OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantData = restaurantData;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public static Order create(Long customerId, RestaurantData restaurantData, List<OrderItem> items) {
        BigDecimal total = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Order(
                null,
                customerId,
                restaurantData,
                items,
                total,
                OrderStatus.AGUARDANDO_CONFIRMACAO
        );
    }

    public void markAsPaid() {
        this.status = OrderStatus.PAGO;
    }

    public void markAsPendingPayment() {
        this.status = OrderStatus.PENDENTE_PAGAMENTO;
    }

    public void confirm() {
        if (this.status != OrderStatus.AGUARDANDO_CONFIRMACAO) {
            throw new InvalidOrderStatusException("Only orders waiting for confirmation can be confirmed");
        }
        this.status = OrderStatus.CRIADO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public RestaurantData getRestaurantData() {
        return restaurantData;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }
}