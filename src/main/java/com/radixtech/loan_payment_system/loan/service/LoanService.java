package com.radixtech.loan_payment_system.loan.service;

import com.radixtech.loan_payment_system.exception.ResourceNotFoundException;
import com.radixtech.loan_payment_system.loan.model.Loan;
import com.radixtech.loan_payment_system.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Transactional
    public Loan createLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
    }

    @Transactional
    public Loan updateLoanBalance(Long loanId, Double paymentAmount) {
        Loan loan = getLoanById(loanId);

        Double newBalance = loan.getRemainingBalance() - paymentAmount;
        loan.setRemainingBalance(Math.max(0.0, newBalance));

        return loanRepository.save(loan);
    }
}