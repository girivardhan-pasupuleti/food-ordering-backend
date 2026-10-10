package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.FoodItem;
import com.foodordering.food_ordering_system.entity.Review;
import com.foodordering.food_ordering_system.entity.User;

import com.foodordering.food_ordering_system.repository.FoodItemRepository;
import com.foodordering.food_ordering_system.repository.ReviewRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            FoodItemRepository foodItemRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public void addReview(
            String email,
            Long foodItemId,
            int rating,
            String comment) {

        if (rating < 1 || rating > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() ->
                        new RuntimeException("Food item not found"));

        Review review = new Review();

        review.setCustomer(customer);
        review.setFoodItem(foodItem);
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public List<Review> getFoodReviews(Long foodItemId) {

        return reviewRepository
                .findByFoodItemIdOrderByReviewDateDesc(foodItemId);
    }
}