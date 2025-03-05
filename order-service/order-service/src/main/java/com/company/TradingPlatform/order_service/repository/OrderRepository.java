package com.company.TradingPlatform.order_service.repository;

import com.company.TradingPlatform.order_service.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.orderType = 'SELL' AND o.price <= :buyPrice AND o.status = 'PENDING' ORDER BY o.price ASC, o.createdAt ASC")
    List<Order> findMatchingSellOrders(@Param("symbol") String symbol, @Param("buyPrice") BigDecimal buyPrice);

    @Query("SELECT o FROM Order o WHERE o.symbol = :symbol AND o.orderType = 'BUY' AND o.price >= :sellPrice AND o.status = 'PENDING' ORDER BY o.price DESC, o.createdAt ASC")
    List<Order> findMatchingBuyOrders(@Param("symbol") String symbol, @Param("sellPrice") BigDecimal sellPrice);

}
