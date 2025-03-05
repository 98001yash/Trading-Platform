package com.company.TradingPlatform.order_service.service;


import com.company.TradingPlatform.order_service.dtos.OrderEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEventDto> kafkaTemplate;
    private static final String TOPIC="order-events";

    public void publishOrderEvent(OrderEventDto orderEvent){
        log.info("Publishing order event: {}",orderEvent);
        kafkaTemplate.send(TOPIC,orderEvent);
    }
}
