package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByOwnerEmail(String email);
}