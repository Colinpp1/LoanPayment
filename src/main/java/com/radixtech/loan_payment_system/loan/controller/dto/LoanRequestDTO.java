package com.radixtech.loan_payment_system.loan.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanRequestDTO {
    @NotNull(message = "Loan amount is required")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    private Double loanAmount;

    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer term;
}