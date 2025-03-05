package com.company.TradingPlatform.order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventDto {
    private Long orderId;
    private Long userId;
    private String orderType; // BUY or SELL
    private String status;    // PENDING, COMPLETED, FAILED
    private BigDecimal price;
    private BigDecimal quantity;
}