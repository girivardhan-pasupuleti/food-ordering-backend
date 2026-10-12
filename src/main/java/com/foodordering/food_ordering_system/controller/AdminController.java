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

import com.foodordering.food_ordering_system.entity.Restaurant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final FoodItemRepository foodItemRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(
            UserRepository userRepository,
            RestaurantRepository restaurantRepository,
            CategoryRepository categoryRepository,
            OrderRepository orderRepository,
            FoodItemRepository foodItemRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.foodItemRepository = foodItemRepository;
        this.passwordEncoder = passwordEncoder;
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

    // ================= RESTAURANT MANAGEMENT =================

    @PostMapping("/restaurants/add")
    public String addRestaurant(
            @RequestParam String name,
            @RequestParam String ownerName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam(required = false) String phone,
            RedirectAttributes redirectAttributes) {

        try {
            User owner;
            if (userRepository.existsByEmail(email)) {
                owner = userRepository.findByEmail(email).orElseThrow();
                // Check if user already owns a restaurant
                if (restaurantRepository.findByOwnerEmail(email).isPresent()) {
                    redirectAttributes.addFlashAttribute("error", "User with email '" + email + "' already owns a restaurant!");
                    return "redirect:/admin/restaurants";
                }
                // Upgrade to RESTAURANT role
                owner.setRole(Role.RESTAURANT);
                userRepository.save(owner);
            } else {
                owner = new User();
                owner.setName(ownerName);
                owner.setEmail(email);
                owner.setPassword(passwordEncoder.encode(password));
                owner.setRole(Role.RESTAURANT);
                owner = userRepository.save(owner);
            }

            Restaurant restaurant = new Restaurant();
            restaurant.setName(name);
            restaurant.setAddress(address);
            restaurant.setPhone(phone != null ? phone : "");
            restaurant.setEmail(email);
            restaurant.setOwner(owner);
            restaurantRepository.save(restaurant);

            redirectAttributes.addFlashAttribute("success", "Restaurant '" + name + "' added successfully! Owner login: " + email);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add restaurant: " + e.getMessage());
        }

        return "redirect:/admin/restaurants";
    }

    @PostMapping("/restaurants/{id}/delete")
    public String deleteRestaurant(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            if (restaurantRepository.existsById(id)) {
                restaurantRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Restaurant deleted successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Restaurant not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete restaurant (it may have active menu items or orders): " + e.getMessage());
        }

        return "redirect:/admin/restaurants";
    }

    // ================= USER & ADMIN ACCESS MANAGEMENT =================

    @PostMapping("/users/add")
    public String addUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes) {

        try {
            if (userRepository.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "User with email '" + email + "' already exists!");
                return "redirect:/admin/users";
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "User '" + name + "' created successfully with role " + role + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/change-role")
    public String changeUserRole(
            @PathVariable Long id,
            @RequestParam Role role,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
            user.setRole(role);
            userRepository.save(user);

            // If changed to RESTAURANT, ensure they have a linked Restaurant profile so dashboard works immediately
            if (role == Role.RESTAURANT && restaurantRepository.findByOwnerEmail(user.getEmail()).isEmpty()) {
                Restaurant newRestaurant = new Restaurant();
                newRestaurant.setName(user.getName() + "'s Kitchen");
                newRestaurant.setAddress("Main Street");
                newRestaurant.setPhone("9876543210");
                newRestaurant.setEmail(user.getEmail());
                newRestaurant.setOwner(user);
                restaurantRepository.save(newRestaurant);
            }

            redirectAttributes.addFlashAttribute("success", "Updated " + user.getName() + "'s role to " + role + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update role: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}
