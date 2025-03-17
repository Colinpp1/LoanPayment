package com.radixtech.loan_payment_system.payment.controller;

import com.radixtech.loan_payment_system.payment.controller.dto.PaymentRequestDTO;
import com.radixtech.loan_payment_system.payment.model.Payment;
import com.radixtech.loan_payment_system.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setLoanId(paymentRequestDTO.getLoanId());
        payment.setPaymentAmount(paymentRequestDTO.getPaymentAmount());

        Payment createdPayment = paymentService.createPayment(payment);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }
}
