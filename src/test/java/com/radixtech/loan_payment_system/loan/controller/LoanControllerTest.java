package com.radixtech.loan_payment_system.loan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radixtech.loan_payment_system.loan.controller.dto.LoanRequestDTO;
import com.radixtech.loan_payment_system.loan.model.Loan;
import com.radixtech.loan_payment_system.loan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    private LoanController loanController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Loan testLoan;
    private LoanRequestDTO loanRequestDTO;

    @BeforeEach
    void setUp() {
        loanController = new LoanController(loanService);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();

        testLoan = new Loan();
        testLoan.setLoanId(1L);
        testLoan.setLoanAmount(10000.0);
        testLoan.setTerm(12);
        testLoan.setRemainingBalance(10000.0);

        loanRequestDTO = new LoanRequestDTO();
        loanRequestDTO.setLoanAmount(10000.0);
        loanRequestDTO.setTerm(12);
    }

    @Test
    void testCreateLoan() throws Exception {
        when(loanService.createLoan(any(Loan.class))).thenReturn(testLoan);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.loanId").value(testLoan.getLoanId()))
                .andExpect(jsonPath("$.loanAmount").value(testLoan.getLoanAmount()))
                .andExpect(jsonPath("$.term").value(testLoan.getTerm()))
                .andExpect(jsonPath("$.remainingBalance").value(testLoan.getRemainingBalance()));
    }

    @Test
    void testGetLoan() throws Exception {
        when(loanService.getLoanById(1L)).thenReturn(testLoan);

        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanId").value(testLoan.getLoanId()))
                .andExpect(jsonPath("$.loanAmount").value(testLoan.getLoanAmount()))
                .andExpect(jsonPath("$.term").value(testLoan.getTerm()))
                .andExpect(jsonPath("$.remainingBalance").value(testLoan.getRemainingBalance()));
    }

    @Test
    void testCreateLoanWithInvalidData() throws Exception {
        loanRequestDTO.setLoanAmount(null);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loanRequestDTO)))
                .andExpect(status().isBadRequest());
    }
}
