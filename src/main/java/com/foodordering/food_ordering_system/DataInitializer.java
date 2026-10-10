package com.foodordering.food_ordering_system;

import com.foodordering.food_ordering_system.entity.Category;
import com.foodordering.food_ordering_system.entity.Restaurant;
import com.foodordering.food_ordering_system.entity.Role;
import com.foodordering.food_ordering_system.entity.User;
import com.foodordering.food_ordering_system.repository.CategoryRepository;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeData(
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            CategoryRepository categoryRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            User restaurantUser;

            if (userRepository.existsByEmail("restaurant@food.com")) {

                restaurantUser =
                        userRepository
                                .findByEmail("restaurant@food.com")
                                .orElseThrow();

            } else {

                restaurantUser = new User();

                restaurantUser.setName("Food Restaurant");
                restaurantUser.setEmail("restaurant@food.com");
                restaurantUser.setPassword(
                        passwordEncoder.encode("restaurant123"));
                restaurantUser.setRole(Role.RESTAURANT);

                restaurantUser =
                        userRepository.save(restaurantUser);
            }

            if (restaurantRepository
                    .findByOwnerEmail("restaurant@food.com")
                    .isEmpty()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setName("Food Restaurant");
                restaurant.setAddress("Hyderabad");
                restaurant.setPhone("9876543210");
                restaurant.setEmail("restaurant@food.com");
                restaurant.setOwner(restaurantUser);

                restaurantRepository.save(restaurant);
            }

            if (!userRepository.existsByEmail("customer@food.com")) {
                User demoCustomer = new User();
                demoCustomer.setName("John Customer");
                demoCustomer.setEmail("customer@food.com");
                demoCustomer.setPassword(passwordEncoder.encode("customer123"));
                demoCustomer.setRole(Role.CUSTOMER);
                userRepository.save(demoCustomer);
            }

            createCategory(categoryRepository, "Biryani");
            createCategory(categoryRepository, "Pizza");
            createCategory(categoryRepository, "Burger");
            createCategory(categoryRepository, "Starters");
            createCategory(categoryRepository, "Desserts");
            createCategory(categoryRepository, "Drinks");
        };
    }

    private void createCategory(
            CategoryRepository categoryRepository,
            String name) {

        if (categoryRepository.findByName(name).isEmpty()) {

            categoryRepository.save(
                    new Category(name)
            );
        }
    }
}