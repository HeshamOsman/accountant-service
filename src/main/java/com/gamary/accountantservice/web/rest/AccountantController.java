package com.gamary.accountantservice.web.rest;


import com.gamary.accountantservice.service.AccountantService;
import com.gamary.paymentplatformcommons.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountantController {

    @Autowired
    private AccountantService accountantService;

    @GetMapping("/accountant-payments")
    public ResponseEntity<Page<PaymentDTO>> getPayments(Pageable pageable,@RequestParam(required = false) Integer amountInCents,
                                                        @RequestParam(required = false) String description,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdDate,
                                                        @RequestParam(required = false) String counterPartyName,
                                                        @RequestParam(required = false) String counterPartyAccountNumber,
                                                        @RequestParam(required = false) String paymentToken,
                                                        @RequestParam(required = false) String tikkieId,
                                                        @RequestParam(required = false) String processedPaymentRequestToken){
        return ResponseEntity.ok(accountantService.getAllPayments(pageable,paymentToken,tikkieId,counterPartyName,counterPartyAccountNumber,
                amountInCents,description,processedPaymentRequestToken,createdDate));
    }
}
