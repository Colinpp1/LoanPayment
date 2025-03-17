package com.radixtech.loan_payment_system.loan.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @NotNull(message = "Loan amount is required")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    private Double loanAmount;

    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Term must be at least 1 month")
    private Integer term;

    private Double remainingBalance;

    @PrePersist
    public void prePersist() {
        this.remainingBalance = this.loanAmount;
    }
}