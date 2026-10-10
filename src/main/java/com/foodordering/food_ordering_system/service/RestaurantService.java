package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.Restaurant;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getRestaurantByOwnerEmail(String email) {

        return restaurantRepository.findByOwnerEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Restaurant not found"));
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}