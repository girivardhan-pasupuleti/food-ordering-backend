package com.foodordering.food_ordering_system.service;

import com.foodordering.food_ordering_system.entity.Order;
import com.foodordering.food_ordering_system.entity.Payment;
import com.foodordering.food_ordering_system.repository.PaymentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment makePayment(
            Order order,
            String paymentMethod) {

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment getPaymentForOrder(Long orderId) {

        return paymentRepository
                .findByOrderId(orderId)
                .orElse(null);
    }
}
