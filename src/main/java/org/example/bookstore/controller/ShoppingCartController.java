package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.shoppingcart.AddBookToCartDto;
import org.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import org.example.bookstore.dto.shoppingcart.UpdateBookQuantityDto;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Controller for Shopping Cart class",
        description = "All endpoints of a Shopping Cart class")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get cart method",
            description = "Returns all items in cart")
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartService.getCart(authentication);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create cart item method",
            description = "Creates book item in cart")
    public ShoppingCartDto create(Authentication authentication,
                              @RequestBody @Valid AddBookToCartDto dto) {
        return shoppingCartService.save(authentication, dto);
    }

    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update cart item method",
            description = "Updates book item quantity in cart")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto update(Authentication authentication, @PathVariable Long id,
                                      @RequestBody @Valid UpdateBookQuantityDto dto) {
        return shoppingCartService.update(authentication, id, dto);
    }

    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete cart item method",
            description = "Deletes book item from cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable Long id) {
        shoppingCartService.delete(authentication, id);
    }
}
