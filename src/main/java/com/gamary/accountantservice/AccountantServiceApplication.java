package com.gamary.accountantservice;

import com.gamary.paymentplatformcommons.dto.PaymentRequestDTO;
import com.gamary.paymentplatformcommons.dto.ProcessedPaymentRequestDTO;
import org.modelmapper.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class AccountantServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountantServiceApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}



}
