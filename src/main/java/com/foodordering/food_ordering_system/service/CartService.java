package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.Cart;
import com.foodordering.food_ordering_system.entity.CartItem;
import com.foodordering.food_ordering_system.entity.FoodItem;
import com.foodordering.food_ordering_system.entity.User;
import com.foodordering.food_ordering_system.repository.CartItemRepository;
import com.foodordering.food_ordering_system.repository.CartRepository;
import com.foodordering.food_ordering_system.repository.FoodItemRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            FoodItemRepository foodItemRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
    }

    private Cart getOrCreateCart(String email) {

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByCustomerEmail(email)
                .orElseGet(() -> cartRepository.save(new Cart(customer)));
    }

    @Transactional
    public void addToCart(String email, Long foodItemId) {

        Cart cart = getOrCreateCart(email);

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new RuntimeException("Food item not found"));

        CartItem cartItem = cartItemRepository
                .findByCartIdAndFoodItemId(cart.getId(), foodItemId)
                .orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setFoodItem(foodItem);
            cartItem.setQuantity(1);

        } else {

            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        cartItemRepository.save(cartItem);
    }

    public Cart getCart(String email) {
        return getOrCreateCart(email);
    }

    public void removeItem(String email, Long cartItemId) {

        Cart cart = getOrCreateCart(email);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getCart().getId().equals(cart.getId())) {
            cartItemRepository.delete(item);
        }
    }

    public void increaseQuantity(String email, Long cartItemId) {

        Cart cart = getOrCreateCart(email);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getCart().getId().equals(cart.getId())) {
            item.setQuantity(item.getQuantity() + 1);
            cartItemRepository.save(item);
        }
    }

    public void decreaseQuantity(String email, Long cartItemId) {

        Cart cart = getOrCreateCart(email);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getCart().getId().equals(cart.getId())) {

            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                cartItemRepository.save(item);
            } else {
                cartItemRepository.delete(item);
            }
        }
    }
}