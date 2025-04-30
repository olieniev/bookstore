package org.example.bookstore.repository;

import java.util.Optional;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCart(Long id, ShoppingCart cart);

    void deleteByIdAndShoppingCart(Long id, ShoppingCart cart);
}
