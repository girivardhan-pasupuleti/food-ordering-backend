package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.FoodItem;
import com.foodordering.food_ordering_system.repository.FoodItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodItem> getFoodItemsByRestaurant(Long restaurantId) {
        return foodItemRepository.findByRestaurantId(restaurantId);
    }

    public List<FoodItem> getAvailableFoodItems() {
        return foodItemRepository.findByAvailableTrue();
    }

    public FoodItem getById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Food item not found"));
    }

    public FoodItem save(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    public void delete(Long id) {
        foodItemRepository.deleteById(id);
    }
}