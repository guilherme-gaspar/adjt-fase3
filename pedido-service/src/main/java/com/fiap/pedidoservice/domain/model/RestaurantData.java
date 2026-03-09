package com.fiap.pedidoservice.domain.model;

public class RestaurantData {

    private final Long restaurantId;
    private final String restaurantName;

    public RestaurantData(Long restaurantId, String restaurantName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}