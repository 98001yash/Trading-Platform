package com.company.TradingPlatform.order_service.service;



import com.company.TradingPlatform.order_service.Entity.Order;
import com.company.TradingPlatform.order_service.dtos.OrderRequestDto;
import com.company.TradingPlatform.order_service.dtos.OrderResponseDto;
import com.company.TradingPlatform.order_service.dtos.OrderEventDto;
import com.company.TradingPlatform.order_service.enums.OrderStatus;
import com.company.TradingPlatform.order_service.exceptions.BadRequestException;
import com.company.TradingPlatform.order_service.exceptions.ResourceNotFoundException;

import com.company.TradingPlatform.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final OrderEventProducer orderEventProducer;

    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto){
        log.info("Placing order for userId: {}", orderRequestDto.getUserId());

        if(orderRequestDto.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new BadRequestException("Price must be greater than Zero");
        }
        if(orderRequestDto.getQuantity().compareTo(BigDecimal.ONE) < 0){
            throw new BadRequestException("Quantity must be at least 1");
        }

        Order order = Order.builder()
                .userId(orderRequestDto.getUserId())
                .symbol(orderRequestDto.getSymbol())
                .price(orderRequestDto.getPrice())
                .quantity(orderRequestDto.getQuantity())
                .orderType(orderRequestDto.getOrderType())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Order placed successfully: {}", savedOrder.getId());

        // Publish event to Kafka
        OrderEventDto orderEvent = new OrderEventDto(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getOrderType().name(),
                savedOrder.getStatus().name(),
                savedOrder.getPrice(),
                savedOrder.getQuantity()
        );

        orderEventProducer.publishOrderEvent(orderEvent);
        log.info("Order event published: {}", orderEvent);

        return modelMapper.map(savedOrder, OrderResponseDto.class);
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId){
        log.info("Fetching orders for userId : {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        if(orders.isEmpty()){
            throw new ResourceNotFoundException("No orders found for user Id : " + userId);
        }
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponseDto.class))
                .collect(Collectors.toList());
    }
}
