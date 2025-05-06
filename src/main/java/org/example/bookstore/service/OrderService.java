package org.example.bookstore.service;

import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto createOrder(Authentication authentication, CreateOrderRequestDto requestDto);

    Page<OrderDto> getOrders(Authentication authentication, Pageable pageable);

    Page<OrderItemDto> getOrderItems(Authentication authentication,
                                     Long orderId,
                                     Pageable pageable
    );

    OrderItemDto getOrderItem(Authentication authentication, Long orderId, Long id);

    OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto requestDto);
}
