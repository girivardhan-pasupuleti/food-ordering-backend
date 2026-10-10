package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.entity.Category;
import com.foodordering.food_ordering_system.entity.FoodItem;
import com.foodordering.food_ordering_system.entity.Restaurant;
import com.foodordering.food_ordering_system.repository.CategoryRepository;
import com.foodordering.food_ordering_system.service.FoodItemService;
import com.foodordering.food_ordering_system.service.RestaurantService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final FoodItemService foodItemService;
    private final CategoryRepository categoryRepository;

    public RestaurantController(
            RestaurantService restaurantService,
            FoodItemService foodItemService,
            CategoryRepository categoryRepository) {

        this.restaurantService = restaurantService;
        this.foodItemService = foodItemService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        List<FoodItem> foodItems =
                foodItemService.getFoodItemsByRestaurant(
                        restaurant.getId());

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("foodItems", foodItems);

        return "restaurant/dashboard";
    }

    @GetMapping("/menu")
    public String menu(
            Authentication authentication,
            Model model) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        List<FoodItem> foodItems =
                foodItemService.getFoodItemsByRestaurant(
                        restaurant.getId());

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("foodItems", foodItems);

        return "restaurant/menu";
    }

    @GetMapping("/add-food")
    public String showAddFoodPage(Model model) {

        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("categories",
                categoryRepository.findAll());

        return "restaurant/add-food";
    }

    @PostMapping("/add-food")
    public String addFood(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            Authentication authentication) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));

        FoodItem foodItem = new FoodItem();

        foodItem.setName(name);
        foodItem.setDescription(description);
        foodItem.setPrice(price);
        foodItem.setAvailable(true);
        foodItem.setRestaurant(restaurant);
        foodItem.setCategory(category);

        foodItemService.save(foodItem);

        return "redirect:/restaurant/menu";
    }

    @GetMapping("/edit-food/{id}")
    public String showEditFoodPage(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        FoodItem foodItem = foodItemService.getById(id);

        if (!foodItem.getRestaurant()
                .getId()
                .equals(restaurant.getId())) {

            return "redirect:/restaurant/menu";
        }

        model.addAttribute("foodItem", foodItem);
        model.addAttribute("categories",
                categoryRepository.findAll());

        return "restaurant/edit-food";
    }

    @PostMapping("/edit-food/{id}")
    public String editFood(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam BigDecimal price,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Boolean available,
            Authentication authentication) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        FoodItem foodItem = foodItemService.getById(id);

        if (!foodItem.getRestaurant()
                .getId()
                .equals(restaurant.getId())) {

            return "redirect:/restaurant/menu";
        }

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));

        foodItem.setName(name);
        foodItem.setDescription(description);
        foodItem.setPrice(price);
        foodItem.setCategory(category);
        foodItem.setAvailable(Boolean.TRUE.equals(available));

        foodItemService.save(foodItem);

        return "redirect:/restaurant/menu";
    }

    @PostMapping("/delete-food/{id}")
    public String deleteFood(
            @PathVariable Long id,
            Authentication authentication) {

        Restaurant restaurant =
                restaurantService.getRestaurantByOwnerEmail(
                        authentication.getName());

        FoodItem foodItem = foodItemService.getById(id);

        if (foodItem.getRestaurant()
                .getId()
                .equals(restaurant.getId())) {

            foodItemService.delete(id);
        }

        return "redirect:/restaurant/menu";
    }
}