package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.Restaurant;
import com.foodordering.food_ordering_system.entity.User;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant getRestaurantByOwnerEmail(String email) {
        return restaurantRepository.findByOwnerEmail(email)
                .orElseGet(() -> {
                    // Auto-heal/create restaurant profile if missing for this user
                    User user = userRepository.findByEmail(email).orElse(null);
                    Restaurant newRestaurant = new Restaurant();
                    String displayName = (user != null && user.getName() != null && !user.getName().isBlank())
                            ? user.getName() + "'s Kitchen"
                            : "Partner Restaurant";
                    newRestaurant.setName(displayName);
                    newRestaurant.setAddress("Main Street");
                    newRestaurant.setPhone("9876543210");
                    newRestaurant.setEmail(email);
                    newRestaurant.setOwner(user);
                    return restaurantRepository.save(newRestaurant);
                });
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}
