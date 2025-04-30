package org.example.bookstore.service;

import org.example.bookstore.dto.shoppingcart.AddBookToCartDto;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.dto.shoppingcart.UpdateBookQuantityDto;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getCart(Authentication authentication);

    ShoppingCartDto save(Authentication authentication, AddBookToCartDto dto);

    ShoppingCartDto update(Authentication authentication,
                           Long id, UpdateBookQuantityDto dto);

    void delete(Authentication authentication, Long id);

    ShoppingCart createShoppingCart(User user);

    void clearShoppingCart(ShoppingCart cart);
}
