package com.radixtech.loan_payment_system.loan.service;

import com.radixtech.loan_payment_system.exception.ResourceNotFoundException;
import com.radixtech.loan_payment_system.loan.model.Loan;
import com.radixtech.loan_payment_system.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    private LoanService loanService;

    private Loan testLoan;

    @BeforeEach
    void setUp() {
        loanService = new LoanService(loanRepository);

        testLoan = new Loan();
        testLoan.setLoanId(1L);
        testLoan.setLoanAmount(10000.0);
        testLoan.setTerm(12);
        testLoan.setRemainingBalance(10000.0);
    }

    @Test
    void testCreateLoan() {
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        Loan newLoan = new Loan();
        newLoan.setLoanAmount(10000.0);
        newLoan.setTerm(12);

        Loan createdLoan = loanService.createLoan(newLoan);

        assertNotNull(createdLoan);
        assertEquals(testLoan.getLoanId(), createdLoan.getLoanId());
        assertEquals(testLoan.getLoanAmount(), createdLoan.getLoanAmount());
        assertEquals(testLoan.getTerm(), createdLoan.getTerm());
        assertEquals(testLoan.getRemainingBalance(), createdLoan.getRemainingBalance());

        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testGetLoanById() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));

        Loan foundLoan = loanService.getLoanById(1L);

        assertNotNull(foundLoan);
        assertEquals(testLoan.getLoanId(), foundLoan.getLoanId());
        assertEquals(testLoan.getLoanAmount(), foundLoan.getLoanAmount());
        assertEquals(testLoan.getTerm(), foundLoan.getTerm());

        verify(loanRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLoanByIdNotFound() {
        when(loanRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            loanService.getLoanById(999L);
        });

        verify(loanRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateLoanBalanceRegularPayment() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        Double paymentAmount = 1000.0;
        Double expectedNewBalance = testLoan.getRemainingBalance() - paymentAmount;

        loanService.updateLoanBalance(1L, paymentAmount);

        assertEquals(expectedNewBalance, testLoan.getRemainingBalance());
        verify(loanRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).save(testLoan);
    }

    @Test
    void testUpdateLoanBalanceOverpayment() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);

        Double paymentAmount = 15000.0;

        loanService.updateLoanBalance(1L, paymentAmount);

        assertEquals(0.0, testLoan.getRemainingBalance());
        verify(loanRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).save(testLoan);
    }
}