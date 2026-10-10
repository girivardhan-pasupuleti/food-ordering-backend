package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomerEmail(String email);
}