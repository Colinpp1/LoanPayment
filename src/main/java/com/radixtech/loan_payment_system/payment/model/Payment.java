package com.radixtech.loan_payment_system.payment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull(message = "Loan ID is required")
    private Long loanId;

    @NotNull(message = "Payment amount is required")
    @Min(value = 1, message = "Payment amount must be greater than 0")
    private Double paymentAmount;

    private LocalDateTime paymentDate;

    @PrePersist
    public void prePersist() {
        this.paymentDate = LocalDateTime.now();
    }
}