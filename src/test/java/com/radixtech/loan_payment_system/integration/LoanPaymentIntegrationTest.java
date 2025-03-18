package com.radixtech.loan_payment_system.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radixtech.loan_payment_system.loan.controller.dto.LoanRequestDTO;
import com.radixtech.loan_payment_system.loan.model.Loan;
import com.radixtech.loan_payment_system.payment.controller.dto.PaymentRequestDTO;
import com.radixtech.loan_payment_system.payment.model.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoanPaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoanAndPaymentFlow() throws Exception {
        // Create a loan
        LoanRequestDTO loanRequest = new LoanRequestDTO();
        loanRequest.setLoanAmount(10000.0);
        loanRequest.setTerm(12);

        MvcResult loanResult = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").exists())
                .andExpect(jsonPath("$.loanAmount").value(10000.0))
                .andExpect(jsonPath("$.term").value(12))
                .andExpect(jsonPath("$.remainingBalance").value(10000.0))
                .andReturn();

        String loanContent = loanResult.getResponse().getContentAsString();
        Loan createdLoan = objectMapper.readValue(loanContent, Loan.class);
        Long loanId = createdLoan.getLoanId();

        // Make a payment
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setLoanId(loanId);
        paymentRequest.setPaymentAmount(1000.0);

        MvcResult paymentResult = mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").exists())
                .andExpect(jsonPath("$.loanId").value(loanId))
                .andExpect(jsonPath("$.paymentAmount").value(1000.0))
                .andReturn();

        String paymentContent = paymentResult.getResponse().getContentAsString();
        Payment createdPayment = objectMapper.readValue(paymentContent, Payment.class);
        assertNotNull(createdPayment.getPaymentId());

        // Verify loan balance is updated
        mockMvc.perform(get("/api/loans/" + loanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(loanId))
                .andExpect(jsonPath("$.remainingBalance").value(9000.0));
    }

    @Test
    void testOverpayment() throws Exception {
        // Create a loan
        LoanRequestDTO loanRequest = new LoanRequestDTO();
        loanRequest.setLoanAmount(5000.0);
        loanRequest.setTerm(12);

        MvcResult loanResult = mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String loanContent = loanResult.getResponse().getContentAsString();
        Loan createdLoan = objectMapper.readValue(loanContent, Loan.class);
        Long loanId = createdLoan.getLoanId();

        // Make an overpayment
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setLoanId(loanId);
        paymentRequest.setPaymentAmount(6000.0);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated());

        // Verify loan balance is zero and not negative
        mockMvc.perform(get("/api/loans/" + loanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(loanId))
                .andExpect(jsonPath("$.remainingBalance").value(0.0));
    }

    @Test
    void testInvalidLoanRequest() throws Exception {
        LoanRequestDTO invalidLoanRequest = new LoanRequestDTO();
        invalidLoanRequest.setLoanAmount(-1000.0);
        invalidLoanRequest.setTerm(12);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoanRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidPaymentRequest() throws Exception {
        PaymentRequestDTO invalidPaymentRequest = new PaymentRequestDTO();
        invalidPaymentRequest.setLoanId(999L);
        invalidPaymentRequest.setPaymentAmount(-100.0);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPaymentRequest)))
                .andExpect(status().isBadRequest());
    }
}