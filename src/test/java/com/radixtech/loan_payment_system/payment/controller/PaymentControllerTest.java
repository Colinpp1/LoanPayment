package com.radixtech.loan_payment_system.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radixtech.loan_payment_system.payment.controller.dto.PaymentRequestDTO;
import com.radixtech.loan_payment_system.payment.model.Payment;
import com.radixtech.loan_payment_system.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Payment testPayment;
    private PaymentRequestDTO paymentRequestDTO;

    @BeforeEach
    void setUp() {
        testPayment = new Payment();
        testPayment.setPaymentId(1L);
        testPayment.setLoanId(1L);
        testPayment.setPaymentAmount(500.0);
        testPayment.setPaymentDate(LocalDateTime.now());

        paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setLoanId(1L);
        paymentRequestDTO.setPaymentAmount(500.0);
    }

    @Test
    void testCreatePayment() throws Exception {
        when(paymentService.createPayment(any(Payment.class))).thenReturn(testPayment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(testPayment.getPaymentId()))
                .andExpect(jsonPath("$.loanId").value(testPayment.getLoanId()))
                .andExpect(jsonPath("$.paymentAmount").value(testPayment.getPaymentAmount()));
    }

    @Test
    void testCreatePaymentWithInvalidData() throws Exception {
        paymentRequestDTO.setPaymentAmount(null);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
