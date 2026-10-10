package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.Cart;
import com.foodordering.food_ordering_system.entity.CartItem;
import com.foodordering.food_ordering_system.entity.Order;
import com.foodordering.food_ordering_system.entity.OrderItem;
import com.foodordering.food_ordering_system.entity.OrderStatus;
import com.foodordering.food_ordering_system.entity.Restaurant;
import com.foodordering.food_ordering_system.entity.User;

import com.foodordering.food_ordering_system.repository.CartRepository;
import com.foodordering.food_ordering_system.repository.OrderRepository;
import com.foodordering.food_ordering_system.repository.RestaurantRepository;
import com.foodordering.food_ordering_system.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public OrderService(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            UserRepository userRepository,
            RestaurantRepository restaurantRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Order placeOrder(String email, String deliveryAddress) {

        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepository.findByCustomerEmail(email)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }

        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new RuntimeException("Delivery address is required");
        }

        Restaurant restaurant =
                cart.getItems().get(0).getFoodItem().getRestaurant();

        // One order belongs to one restaurant.
        for (CartItem cartItem : cart.getItems()) {

            if (!cartItem.getFoodItem()
                    .getRestaurant()
                    .getId()
                    .equals(restaurant.getId())) {

                throw new RuntimeException(
                        "Please order food from only one restaurant at a time."
                );
            }
        }

        Order order = new Order();

        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAddress(deliveryAddress.trim());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setFoodItem(cartItem.getFoodItem());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getFoodItem().getPrice());

            BigDecimal subtotal =
                    cartItem.getFoodItem()
                            .getPrice()
                            .multiply(
                                    BigDecimal.valueOf(cartItem.getQuantity())
                            );

            total = total.add(subtotal);

            order.addItem(orderItem);
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // Clear the cart after successful order.
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getCustomerOrders(String email) {
        return orderRepository
                .findByCustomerEmailOrderByOrderDateDesc(email);
    }

    public Order getCustomerOrder(Long orderId, String email) {

        return orderRepository
                .findByIdAndCustomerEmail(orderId, email)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getRestaurantOrders(String email) {

        Restaurant restaurant = restaurantRepository
                .findByOwnerEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Restaurant not found"
                ));

        return orderRepository
                .findByRestaurantIdOrderByOrderDateDesc(
                        restaurant.getId()
                );
    }

    public void updateOrderStatus(
            Long orderId,
            String email,
            OrderStatus status) {

        Restaurant restaurant = restaurantRepository
                .findByOwnerEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Restaurant not found"
                ));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(
                        "Order not found"
                ));

        if (!order.getRestaurant()
                .getId()
                .equals(restaurant.getId())) {

            throw new RuntimeException(
                    "You cannot update this order."
            );
        }

        order.setStatus(status);

        orderRepository.save(order);
    }
}