package org.example.bookstore.repository;

import java.util.Optional;
import org.example.bookstore.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByOrderIdAndOrderUserId(Long orderId, Long id, Pageable pageable);

    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long orderId, Long id1);
}
