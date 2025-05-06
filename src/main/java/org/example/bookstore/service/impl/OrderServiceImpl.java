package org.example.bookstore.service.impl;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.OrderItemDto;
import org.example.bookstore.dto.order.UpdateOrderRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.exception.OrderProcessingException;
import org.example.bookstore.mapper.OrderItemMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.OrderItemRepository;
import org.example.bookstore.repository.OrderRepository;
import org.example.bookstore.repository.ShoppingCartRepository;
import org.example.bookstore.service.OrderService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public OrderDto createOrder(Authentication authentication, CreateOrderRequestDto requestDto) {
        User user = findUser(authentication);
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId());
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Can't create an order from an empty cart!");
        }
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setOrderItems(mapCartToOrderItems(cart, order));
        order.setTotal(
                order.getOrderItems().stream()
                .map(item ->
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        shoppingCartService.clearShoppingCart(cart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderDto> getOrders(Authentication authentication, Pageable pageable) {
        return orderRepository.findAllByUserId(findUser(authentication), pageable)
            .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemDto> getOrderItems(
            Authentication authentication,
            Long orderId,
            Pageable pageable
    ) {
        return orderItemRepository.findAllByOrderIdAndOrderUserId(
            orderId, findUser(authentication).getId(), pageable
        )
            .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getOrderItem(Authentication authentication, Long orderId, Long id) {
        return orderItemMapper.toDto(
            orderItemRepository.findByIdAndOrderIdAndOrderUserId(
            id, orderId, findUser(authentication).getId()
        ).orElseThrow(() -> new EntityNotFoundException("Can't find order item with id: " + id)));
    }

    @Override
    public OrderDto updateOrderStatus(Long id, UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                    new EntityNotFoundException("Can't find order with id: " + id));
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private User findUser(Authentication auth) {
        return (User) auth.getPrincipal();
    }

    private Set<OrderItem> mapCartToOrderItems(ShoppingCart cart, Order order) {
        return cart.getCartItems().stream()
            .map(cartItem -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setBook(cartItem.getBook());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getBook().getPrice());
                return orderItem;
            })
            .collect(Collectors.toSet());
    }
}
