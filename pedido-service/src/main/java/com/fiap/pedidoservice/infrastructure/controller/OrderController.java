package com.fiap.pedidoservice.infrastructure.controller;

import com.fiap.pedidoservice.application.dto.OrderOutput;
import com.fiap.pedidoservice.application.usecase.ConfirmOrderUseCase;
import com.fiap.pedidoservice.application.usecase.CreateOrderUseCase;
import com.fiap.pedidoservice.application.usecase.GetOrderByIdUseCase;
import com.fiap.pedidoservice.application.usecase.ListOrdersByCustomerUseCase;
import com.fiap.pedidoservice.infrastructure.controller.dto.CreateOrderRequest;
import com.fiap.pedidoservice.infrastructure.controller.dto.OrderResponse;
import com.fiap.pedidoservice.infrastructure.mapper.OrderControllerMapper;
import com.fiap.pedidoservice.infrastructure.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final ListOrdersByCustomerUseCase listOrdersByCustomerUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final OrderControllerMapper mapper;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                           GetOrderByIdUseCase getOrderByIdUseCase,
                           ListOrdersByCustomerUseCase listOrdersByCustomerUseCase,
                           ConfirmOrderUseCase confirmOrderUseCase,
                           OrderControllerMapper mapper) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.listOrdersByCustomerUseCase = listOrdersByCustomerUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request,
                                                @AuthenticationPrincipal AuthenticatedUser user) {
        OrderOutput output = createOrderUseCase.execute(mapper.toCommand(request, user.userId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(output));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id,
                                                  @AuthenticationPrincipal AuthenticatedUser user) {
        OrderOutput output = getOrderByIdUseCase.execute(id, user.userId());
        return ResponseEntity.ok(mapper.toResponse(output));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findMyOrders(@AuthenticationPrincipal AuthenticatedUser user) {
        List<OrderResponse> responses = listOrdersByCustomerUseCase.execute(user.userId()).stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable Long id,
                                                 @AuthenticationPrincipal AuthenticatedUser user) {
        OrderOutput output = confirmOrderUseCase.execute(id, user.userId());
        return ResponseEntity.ok(mapper.toResponse(output));
    }
}