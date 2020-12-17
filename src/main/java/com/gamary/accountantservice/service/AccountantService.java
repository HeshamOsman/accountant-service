package com.gamary.accountantservice.service;


import com.gamary.paymentplatformcommons.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AccountantService {

//    ProcessedPaymentRequest saveProcessedPaymentRequest(ProcessedPaymentRequestDTO processedPaymentRequestDTO);

    Page<PaymentDTO> getAllPayments(Pageable pageable, String paymentToken, String tikkieId, String counterPartyName, String counterPartyAccountNumber,
                                    Integer amountInCents, String description, String processedPaymentRequestToken,
                                    LocalDate createdDate);

}
