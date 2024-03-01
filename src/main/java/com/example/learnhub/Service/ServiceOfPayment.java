package com.example.learnhub.Service;

import com.example.learnhub.Entity.Payment;
import com.example.learnhub.Repository.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfPayment {
    private final PaymentRepository paymentRepository;

    @Autowired
    public ServiceOfPayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
