package org.example.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.shoppingcart.AddBookToCartDto;
import org.example.bookstore.dto.shoppingcart.CartItemDto;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.dto.shoppingcart.UpdateBookQuantityDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CartItemMapper;
import org.example.bookstore.mapper.ShoppingCartMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.repository.CartItemRepository;
import org.example.bookstore.repository.ShoppingCartRepository;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartMapper.toDto(findCart(authentication));
    }

    @Override
    public CartItemDto save(Authentication authentication, AddBookToCartDto dto) {
        ShoppingCart cart = findCart(authentication);
        Book book = bookRepository.findById(dto.id()).orElseThrow(
                () -> new EntityNotFoundException("No book found with id: " + dto.id())
        );
        CartItem item = new CartItem();
        item.setShoppingCart(cart);
        item.setBook(book);
        item.setQuantity(dto.quantity());
        cart.getCartItems().add(item);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }

    @Override
    public CartItemDto update(Authentication authentication, Long id, UpdateBookQuantityDto dto) {
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCart(id, findCart(authentication)).orElseThrow(
                    () -> new EntityNotFoundException("Can't find user cart item with id: " + id)
        );
        cartItem.setQuantity(dto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Authentication authentication, Long id) {
        cartItemRepository.deleteByIdAndShoppingCart(id, findCart(authentication));
    }

    private ShoppingCart findCart(Authentication authentication) {
        return shoppingCartRepository.findByUser((User) authentication.getPrincipal());
    }
}
