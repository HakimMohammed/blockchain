package com.inventory.backend.dtos.exchange;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class TradeRequest {

    @NotNull(message = "Sender id is required")
    private String senderId;

    @NotNull(message = "Receiver id is required")
    private String receiverId;

    @NotNull(message = "Product id is required")
    private String productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
