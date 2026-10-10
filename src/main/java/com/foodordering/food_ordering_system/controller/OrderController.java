package com.foodordering.food_ordering_system.controller;

import com.foodordering.food_ordering_system.entity.Order;
import com.foodordering.food_ordering_system.entity.OrderStatus;
import com.foodordering.food_ordering_system.service.CartService;
import com.foodordering.food_ordering_system.service.OrderService;
import com.foodordering.food_ordering_system.service.PaymentService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentService paymentService;

    public OrderController(
            OrderService orderService,
            CartService cartService,
            PaymentService paymentService) {

        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    // =========================
    // CUSTOMER CHECKOUT
    // =========================

    @GetMapping("/customer/checkout")
    public String checkout(
            Authentication authentication,
            Model model) {

        var cart = cartService.getCart(authentication.getName());

        if (cart.getItems().isEmpty()) {
            return "redirect:/customer/cart";
        }

        model.addAttribute("cart", cart);

        return "customer/checkout";
    }

    @PostMapping("/customer/order/place")
    public String placeOrder(
            @RequestParam String deliveryAddress,
            @RequestParam String paymentMethod,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            Order order = orderService.placeOrder(
                    authentication.getName(),
                    deliveryAddress
            );

            paymentService.makePayment(
                    order,
                    paymentMethod
            );

            return "redirect:/customer/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/customer/checkout";
        }
    }
    // =========================
    // CUSTOMER ORDERS
    // =========================

    @GetMapping("/customer/orders")
    public String customerOrders(
            Authentication authentication,
            Model model) {

        model.addAttribute(
                "orders",
                orderService.getCustomerOrders(
                        authentication.getName()
                )
        );

        return "customer/orders";
    }

    @GetMapping("/customer/orders/{id}")
    public String orderDetails(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Order order = orderService.getCustomerOrder(
                id,
                authentication.getName()
        );

        model.addAttribute("order", order);
        model.addAttribute(
        "payment",
        paymentService.getPaymentForOrder(order.getId())
);
        return "customer/order-details";
    }

    // =========================
    // RESTAURANT ORDERS
    // =========================

    @GetMapping("/restaurant/orders")
    public String restaurantOrders(
            Authentication authentication,
            Model model) {

        model.addAttribute(
                "orders",
                orderService.getRestaurantOrders(
                        authentication.getName()
                )
        );

        return "restaurant/orders";
    }

    @PostMapping("/restaurant/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            Authentication authentication) {

        orderService.updateOrderStatus(
                id,
                authentication.getName(),
                status
        );

        return "redirect:/restaurant/orders";
    }
}