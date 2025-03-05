package com.company.TradingPlatform.order_service.service;


import com.company.TradingPlatform.order_service.Entity.Order;
import com.company.TradingPlatform.order_service.dtos.OrderEventDto;
import com.company.TradingPlatform.order_service.exceptions.ResourceNotFoundException;
import com.company.TradingPlatform.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderRepository orderRepository;
    private final OrderMatchingService orderMatchingService;

    @KafkaListener(topics = "order-events", groupId = "order-matching")
    public void consumeOrderPlacedEvent(OrderEventDto event) {
        log.info("Received new order: {}", event.getOrderId());
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));


        orderMatchingService.matchOrders(order);
    }

}
