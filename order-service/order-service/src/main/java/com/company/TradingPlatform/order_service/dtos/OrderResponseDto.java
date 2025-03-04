package com.company.TradingPlatform.order_service.dtos;


import com.company.TradingPlatform.order_service.enums.OrderStatus;
import com.company.TradingPlatform.order_service.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;
    private Long userId;
    private String symbol;
    private BigDecimal Price;
    private BigDecimal quantity;
    private OrderType orderType;
    private OrderStatus status;
    private LocalDateTime createdAt;
}
