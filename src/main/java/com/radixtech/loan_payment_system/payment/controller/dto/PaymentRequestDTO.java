package com.radixtech.loan_payment_system.payment.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    @NotNull(message = "Loan ID is required")
    private Long loanId;

    @NotNull(message = "Payment amount is required")
    @Min(value = 1, message = "Payment amount must be greater than 0")
    private Double paymentAmount;
}