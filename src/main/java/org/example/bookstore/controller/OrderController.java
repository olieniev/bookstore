package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.dto.order.UpdateOrderRequestDto;
import org.example.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Controller for Order class",
        description = "All methods of Order controller")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create order method",
            description = "Creates an order with given shipping address")
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(authentication, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get orders method",
            description = "Returns all orders")
    public Page<OrderDto> getOrders(Authentication authentication,
                                    Pageable pageable) {
        return orderService.getOrders(authentication, pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order method",
            description = "Returns all items of an order by order id")
    public Page<OrderItemDto> getOrderItems(Authentication authentication,
                                            @PathVariable Long orderId, Pageable pageable) {
        return orderService.getOrderItems(authentication, orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item method",
            description = "Returns order item by order id and order item id")
    public OrderItemDto getOrderItem(Authentication authentication,
                                     @PathVariable Long orderId, @PathVariable Long id) {
        return orderService.getOrderItem(authentication, orderId, id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status method",
            description = "Updates order status by id")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}
