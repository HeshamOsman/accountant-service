package com.gamary.accountantservice.repository;

import com.gamary.accountantservice.entity.ProcessedPaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedPaymentRequestRepository extends JpaRepository<ProcessedPaymentRequest,String> {
}
