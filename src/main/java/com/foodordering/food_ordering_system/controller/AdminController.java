package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.entity.Role;
import com.foodordering.food_ordering_system.entity.User;
import com.foodordering.food_ordering_system.repository.CategoryRepository;
import com.foodordering.food_ordering_system.repository.FoodItemRepository;
import com.foodordering.food_ordering_system.repository.OrderRepository;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final FoodItemRepository foodItemRepository;

    public AdminController(
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            CategoryRepository categoryRepository,
            OrderRepository orderRepository,
            FoodItemRepository foodItemRepository) {

        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.foodItemRepository = foodItemRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        List<User> customers =
                userRepository.findAll()
                        .stream()
                        .filter(user -> user.getRole() == Role.CUSTOMER)
                        .toList();

        List<User> restaurantUsers =
                userRepository.findAll()
                        .stream()
                        .filter(user -> user.getRole() == Role.RESTAURANT)
                        .toList();

        model.addAttribute("customers", customers);
        model.addAttribute("restaurantUsers", restaurantUsers);
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());

        model.addAttribute("customerCount", customers.size());
        model.addAttribute("restaurantCount", restaurantRepository.findAll().size());
        model.addAttribute("categoryCount", categoryRepository.findAll().size());
        model.addAttribute("orderCount", orderRepository.findAll().size());
        model.addAttribute("foodItemCount", foodItemRepository.findAll().size());

        return "admin/dashboard";
    }

    @GetMapping("/customers")
    public String customers(Model model) {

        List<User> customers =
                userRepository.findAll()
                        .stream()
                        .filter(user -> user.getRole() == Role.CUSTOMER)
                        .toList();

        model.addAttribute("customers", customers);

        return "admin/customers";
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model) {

        model.addAttribute("restaurants", restaurantRepository.findAll());

        return "admin/restaurants";
    }

    @GetMapping("/categories")
    public String categories(Model model) {

        model.addAttribute("categories", categoryRepository.findAll());

        return "admin/categories";
    }

    @GetMapping("/orders")
    public String orders(Model model) {

        model.addAttribute("orders", orderRepository.findAll());

        return "admin/orders";
    }

    @GetMapping("/food-items")
    public String foodItems(Model model) {

        model.addAttribute("foodItems", foodItemRepository.findAll());

        return "admin/food-items";
    }

    @GetMapping("/users")
    public String users(Model model) {

        model.addAttribute("users", userRepository.findAll());

        return "admin/users";
    }
}