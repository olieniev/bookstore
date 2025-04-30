package org.example.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import org.example.bookstore.model.Status;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDate orderDate,
        BigDecimal total,
        Status status
) {
}
