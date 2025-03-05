package com.company.TradingPlatform.order_service.service;

import com.company.TradingPlatform.order_service.Entity.Order;
import com.company.TradingPlatform.order_service.dtos.OrderEventDto;
import com.company.TradingPlatform.order_service.enums.OrderStatus;
import com.company.TradingPlatform.order_service.enums.OrderType;
import com.company.TradingPlatform.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMatchingService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public void matchOrders(Order newOrder){
        log.info("Matching order: {} for user {}",newOrder.getId(),newOrder.getUserId());

        if(newOrder.getOrderType()== OrderType.BUY){
            matchBuyOrder(newOrder);
        }else {
            matchSellOrder(newOrder);
        }
    }

    private void matchBuyOrder(Order buyOrder){
        log.info("Matching BUY order: {}",buyOrder.getId());

        List<Order> sellOrders = orderRepository.findMatchingSellOrders(
                buyOrder.getSymbol(),buyOrder.getPrice());

        for(Order sellOrder:sellOrders){
            executeTrade(buyOrder, sellOrder);
            if(buyOrder.getQuantity().compareTo(BigDecimal.ZERO)==0) break;
        }
    }

    private void matchSellOrder(Order sellOrder){
        log.info("Matching SELL order: {},",sellOrder.getId());
        List<Order> buyOrders = orderRepository.findMatchingBuyOrders(
                sellOrder.getSymbol(), sellOrder.getPrice());
        for(Order buyOrder: buyOrders){
            executeTrade(buyOrder, sellOrder);
            if(sellOrder.getQuantity().compareTo(BigDecimal.ZERO)==0) break;
        }
    }

    private void executeTrade(Order buyOrder, Order sellOrder) {
        BigDecimal tradeQuantity = buyOrder.getQuantity().min(sellOrder.getQuantity());
        BigDecimal tradePrice = sellOrder.getPrice(); // sell Order determines price

        log.info("Executing trade :BUY {} shares @ {} from SELL order {}",
                tradeQuantity, tradePrice, sellOrder.getId());
        buyOrder.setQuantity(buyOrder.getQuantity().subtract(tradeQuantity));
        sellOrder.setQuantity(sellOrder.getQuantity().subtract(tradeQuantity));

        if (buyOrder.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            buyOrder.setStatus(OrderStatus.FILLED);
        } else {
            buyOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
        }

        if (sellOrder.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            sellOrder.setStatus(OrderStatus.FILLED);
        } else {
            sellOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
        orderRepository.save(buyOrder);
        orderRepository.save(sellOrder);


        // publish Order_Executed event
        OrderEventDto event = new OrderEventDto(
                buyOrder.getId(),               // orderId
                buyOrder.getUserId(),           // userId
                buyOrder.getOrderType().name(), // orderType (Convert Enum to String)
                buyOrder.getStatus().name(),    // status (Convert Enum to String)
                tradePrice,                     // price
                tradeQuantity                   // quantity
        );
        orderEventProducer.publishOrderEvent(event);
    }
}
