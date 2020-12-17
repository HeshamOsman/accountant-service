package com.gamary.accountantservice.config;

import com.gamary.paymentplatformcommons.dto.PaymentRequestDTO;
import com.gamary.paymentplatformcommons.dto.ProcessedPaymentRequestDTO;
import org.modelmapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

        Provider<ZonedDateTime> zonedDateTimeProvider = new AbstractProvider<ZonedDateTime>() {
            @Override
            public ZonedDateTime get() {
                return ZonedDateTime.now();
            }
        };

        Converter<String, ZonedDateTime> toZonedDateTime = new AbstractConverter<String, ZonedDateTime>() {
            @Override
            protected ZonedDateTime convert(String source) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(source);
                return zonedDateTime;
            }
        };


        modelMapper.createTypeMap(String.class, ZonedDateTime.class);
        modelMapper.addConverter(toZonedDateTime);
        modelMapper.getTypeMap(String.class, ZonedDateTime.class).setProvider(zonedDateTimeProvider);

        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return LocalDate.now();
            }
        };

        Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(source, format);
                return localDate;
            }
        };


        modelMapper.createTypeMap(String.class, LocalDate.class);
        modelMapper.addConverter(toStringDate);
        modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);
        modelMapper.createTypeMap(PaymentRequestDTO.class, ProcessedPaymentRequestDTO.class).addMapping(PaymentRequestDTO::getId,ProcessedPaymentRequestDTO::setReferenceId);


        return modelMapper;
    }
}
