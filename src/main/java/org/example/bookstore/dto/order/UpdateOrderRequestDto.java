package org.example.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.model.Status;

public record UpdateOrderRequestDto(
        @NotNull
        Status status
) {
}
