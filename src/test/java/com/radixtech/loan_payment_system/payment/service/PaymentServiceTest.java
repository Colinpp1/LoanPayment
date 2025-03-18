package com.radixtech.loan_payment_system.payment.service;

import com.radixtech.loan_payment_system.loan.service.LoanService;
import com.radixtech.loan_payment_system.payment.model.Payment;
import com.radixtech.loan_payment_system.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private LoanService loanService;

    private PaymentService paymentService;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, loanService);

        testPayment = new Payment();
        testPayment.setPaymentId(1L);
        testPayment.setLoanId(1L);
        testPayment.setPaymentAmount(500.0);
        testPayment.setPaymentDate(LocalDateTime.now());
    }

    @Test
    void testCreatePayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        Payment newPayment = new Payment();
        newPayment.setLoanId(1L);
        newPayment.setPaymentAmount(500.0);

        Payment createdPayment = paymentService.createPayment(newPayment);

        assertNotNull(createdPayment);
    }
}