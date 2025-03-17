package com.radixtech.loan_payment_system.payment.service;

import com.radixtech.loan_payment_system.loan.service.LoanService;
import com.radixtech.loan_payment_system.payment.model.Payment;
import com.radixtech.loan_payment_system.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanService loanService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, LoanService loanService) {
        this.paymentRepository = paymentRepository;
        this.loanService = loanService;
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        // First update the loan balance
        loanService.updateLoanBalance(payment.getLoanId(), payment.getPaymentAmount());

        // Then save the payment record
        return paymentRepository.save(payment);
    }
}
