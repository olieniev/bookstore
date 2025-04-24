package org.example.bookstore.service;

import jakarta.validation.Valid;
import org.example.bookstore.dto.shoppingcart.AddBookToCartDto;
import org.example.bookstore.dto.shoppingcart.CartItemDto;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.dto.shoppingcart.UpdateBookQuantityDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getCart(Authentication authentication);

    CartItemDto save(Authentication authentication, @Valid AddBookToCartDto dto);

    CartItemDto update(Authentication authentication, Long id, @Valid UpdateBookQuantityDto dto);

    void delete(Authentication authentication, Long id);
}
