package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByOrderDateDesc(String email);

    Optional<Order> findByIdAndCustomerEmail(Long id, String email);

    List<Order> findByRestaurantIdOrderByOrderDateDesc(Long restaurantId);
}