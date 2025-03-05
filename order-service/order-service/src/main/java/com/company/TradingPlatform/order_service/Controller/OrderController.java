package com.company.TradingPlatform.order_service.Controller;


import com.company.TradingPlatform.order_service.dtos.OrderRequestDto;
import com.company.TradingPlatform.order_service.dtos.OrderResponseDto;
import com.company.TradingPlatform.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<OrderResponseDto> placeOrder( @RequestBody OrderRequestDto orderRequestDto) {
        log.info("Received request to place an order: {}", orderRequestDto);
        OrderResponseDto  orderResponse = orderService.placeOrder(orderRequestDto);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Long userId) {
        log.info("Received request to fetch orders for userId: {}", userId);
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
