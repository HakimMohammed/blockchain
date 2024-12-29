package com.inventory.ui.dtos.exchange;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
public class TradeRequest {
    private String senderId;
    private String receiverId;
    private String productId;
    private int quantity;
}
