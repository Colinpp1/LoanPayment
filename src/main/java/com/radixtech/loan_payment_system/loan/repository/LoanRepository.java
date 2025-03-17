package com.radixtech.loan_payment_system.loan.repository;

import com.radixtech.loan_payment_system.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}