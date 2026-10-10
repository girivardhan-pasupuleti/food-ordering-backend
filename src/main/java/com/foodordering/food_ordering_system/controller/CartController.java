package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.entity.Cart;
import com.foodordering.food_ordering_system.service.CartService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Authentication authentication, Model model) {

        Cart cart = cartService.getCart(authentication.getName());

        model.addAttribute("cart", cart);

        return "customer/cart";
    }

    @PostMapping("/add/{foodItemId}")
    public String addToCart(
            @PathVariable Long foodItemId,
            Authentication authentication) {

        cartService.addToCart(authentication.getName(), foodItemId);

        return "redirect:/customer/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        cartService.removeItem(authentication.getName(), cartItemId);

        return "redirect:/customer/cart";
    }

    @PostMapping("/increase/{cartItemId}")
    public String increaseQuantity(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        cartService.increaseQuantity(authentication.getName(), cartItemId);

        return "redirect:/customer/cart";
    }

    @PostMapping("/decrease/{cartItemId}")
    public String decreaseQuantity(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        cartService.decreaseQuantity(authentication.getName(), cartItemId);

        return "redirect:/customer/cart";
    }
}