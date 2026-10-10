package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findByRestaurantId(Long restaurantId);

    List<FoodItem> findByAvailableTrue();
}