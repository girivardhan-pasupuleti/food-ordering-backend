package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndFoodItemId(Long cartId, Long foodItemId);
}