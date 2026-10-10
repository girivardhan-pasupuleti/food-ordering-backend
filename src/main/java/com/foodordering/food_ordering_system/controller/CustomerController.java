package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.repository.FoodItemRepository;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final RestaurantRepository restaurantRepository;
    private final FoodItemRepository foodItemRepository;

    public CustomerController(
            RestaurantRepository restaurantRepository,
            FoodItemRepository foodItemRepository) {

        this.restaurantRepository = restaurantRepository;
        this.foodItemRepository = foodItemRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute(
                "restaurants",
                restaurantRepository.findAll()
        );

        return "customer/dashboard";
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model) {

        model.addAttribute(
                "restaurants",
                restaurantRepository.findAll()
        );

        return "customer/restaurants";
    }

    @GetMapping("/restaurant/{restaurantId}/menu")
    public String restaurantMenu(
            @PathVariable Long restaurantId,
            Model model) {

        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        model.addAttribute("restaurant", restaurant);

        model.addAttribute(
                "foodItems",
                foodItemRepository.findByRestaurantId(restaurantId)
        );

        return "customer/menu";
    }
}