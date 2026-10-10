package com.foodordering.food_ordering_system.repository;

import com.foodordering.food_ordering_system.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFoodItemIdOrderByReviewDateDesc(Long foodItemId);

    List<Review> findByCustomerEmailOrderByReviewDateDesc(String email);
}