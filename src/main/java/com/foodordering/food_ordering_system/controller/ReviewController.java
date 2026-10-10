package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.service.ReviewService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public String addReview(
            @RequestParam Long foodItemId,
            @RequestParam int rating,
            @RequestParam String comment,
            Authentication authentication) {

        reviewService.addReview(
                authentication.getName(),
                foodItemId,
                rating,
                comment
        );

        return "redirect:/customer/orders";
    }
}