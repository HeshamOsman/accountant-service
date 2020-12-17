package com.gamary.accountantservice.service.impl;

import com.gamary.accountantservice.entity.Payment;
import com.gamary.accountantservice.repository.PaymentRepository;
import com.gamary.accountantservice.service.AccountantService;
import com.gamary.paymentplatformcommons.dto.PaymentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountantServiceImpl implements AccountantService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public AccountantServiceImpl(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable,String paymentToken, String tikkieId, String counterPartyName, String counterPartyAccountNumber,
                                           Integer amountInCents, String description, String processedPaymentRequestToken,
                                           LocalDate createdDate) {

        Example<Payment> example = Example.of(new Payment(paymentToken,tikkieId,counterPartyName,counterPartyAccountNumber,amountInCents,description
                ,processedPaymentRequestToken,createdDate));
        return  paymentRepository.findAll(example,pageable).map(p -> modelMapper.map(p,PaymentDTO.class));
    }
}
