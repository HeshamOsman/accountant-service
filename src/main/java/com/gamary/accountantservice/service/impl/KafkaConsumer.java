package com.gamary.accountantservice.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamary.accountantservice.entity.Payment;
import com.gamary.accountantservice.entity.ProcessedPaymentRequest;
import com.gamary.accountantservice.repository.PaymentRepository;
import com.gamary.accountantservice.repository.ProcessedPaymentRequestRepository;
import com.gamary.paymentplatformcommons.dto.PaymentDTO;
import com.gamary.paymentplatformcommons.dto.PaymentEventNotification;
import com.gamary.paymentplatformcommons.dto.ProcessedPaymentRequestDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Service
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private static final String APP_TOKEN_HEADER = "X-App-Token";
    private static final String API_KEY_HEADER = "API-Key";
    private static final String PAYMENT_REQUEST_API_URL = "/paymentrequests/{paymentRequestToken}";
    @Value("${Tikkie.appToken}")
    private String appToken;
    @Value("${Tikkie.apiKey}")
    private String apiKey;
    @Value("${Tikkie.baseUrl}")
    private String baseUrl;

    private final static String PROCESSED_PAYMENT_REQUESTS_TOPIC = "processed-payment-requests-topic";
    private final static String PAYMENTS_TOPIC = "payments-topic";

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ProcessedPaymentRequestRepository processedPaymentRequestRepository;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    public KafkaConsumer(ModelMapper modelMapper, ObjectMapper objectMapper, ProcessedPaymentRequestRepository processedPaymentRequestRepository,
                         PaymentRepository paymentRepository,RestTemplate restTemplate) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.processedPaymentRequestRepository = processedPaymentRequestRepository;
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = PROCESSED_PAYMENT_REQUESTS_TOPIC, groupId = "accountant-service")
    public void consumeProcessedPaymentRequests(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        processedPaymentRequestRepository.save(modelMapper.map(
                objectMapper.readValue(message,ProcessedPaymentRequestDTO.class), ProcessedPaymentRequest.class));

    }

    @KafkaListener(topics = PAYMENTS_TOPIC, groupId = "accountant-service")
    public void consumePayments(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        var paymentDTO= objectMapper.readValue(message,PaymentDTO.class);

        var payment = modelMapper.map(paymentDTO, Payment.class);

        processedPaymentRequestRepository.findById(paymentDTO.getPaymentRequestToken()).ifPresentOrElse(ppr->{
        payment.setPaymentRequest(ppr);
        paymentRepository.save(payment);
        },() -> {
            payment.setPaymentRequest(retrieveAndSaveProcessedPaymentRequest(paymentDTO.getPaymentRequestToken()));
            paymentRepository.save(payment);
        }  );

    }

    private ProcessedPaymentRequest retrieveAndSaveProcessedPaymentRequest(String paymentRequestToken){
        ProcessedPaymentRequest ppr=null;
        try {
             ppr = processedPaymentRequestRepository.save(modelMapper.map(
                    objectMapper.readValue(callTikkieApi(paymentRequestToken),ProcessedPaymentRequestDTO.class), ProcessedPaymentRequest.class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ppr;
    }

    private String callTikkieApi(String paymentRequestToken)  {

        var headersEntity = new HttpEntity<ProcessedPaymentRequestDTO>(getRequestHeaders());
        return restTemplate.exchange(baseUrl+PAYMENT_REQUEST_API_URL, HttpMethod.GET,headersEntity, String.class,
                paymentRequestToken).getBody();

    }

    private HttpHeaders getRequestHeaders(){
        var headers = new HttpHeaders();
        headers.add(APP_TOKEN_HEADER,appToken);
        headers.add(API_KEY_HEADER,apiKey);
        return headers;
    }
}
