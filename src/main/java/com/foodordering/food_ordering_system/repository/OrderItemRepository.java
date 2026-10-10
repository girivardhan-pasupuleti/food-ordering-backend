package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}