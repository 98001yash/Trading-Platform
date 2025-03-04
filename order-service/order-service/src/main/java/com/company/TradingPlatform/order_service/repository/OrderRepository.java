package com.company.TradingPlatform.order_service.repository;

import com.company.TradingPlatform.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
