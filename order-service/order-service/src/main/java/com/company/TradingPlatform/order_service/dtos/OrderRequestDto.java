package com.company.TradingPlatform.order_service.dtos;


import com.company.TradingPlatform.order_service.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDto {

    private Long userId;
    private String symbol;
    private BigDecimal price;
    private BigDecimal quantity;
    private OrderType orderType;
}
