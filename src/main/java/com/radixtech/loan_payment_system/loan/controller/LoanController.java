package com.radixtech.loan_payment_system.loan.controller;

import com.radixtech.loan_payment_system.loan.controller.dto.LoanRequestDTO;
import com.radixtech.loan_payment_system.loan.model.Loan;
import com.radixtech.loan_payment_system.loan.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequestDTO loanRequestDTO) {
        Loan loan = new Loan();
        loan.setLoanAmount(loanRequestDTO.getLoanAmount());
        loan.setTerm(loanRequestDTO.getTerm());

        Loan createdLoan = loanService.createLoan(loan);
        return new ResponseEntity<>(createdLoan, HttpStatus.CREATED);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long loanId) {
        Loan loan = loanService.getLoanById(loanId);
        return ResponseEntity.ok(loan);
    }
}
