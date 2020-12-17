package com.gamary.accountantservice.entity;


import com.gamary.paymentplatformcommons.constant.PaymentRequestStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Data
public class ProcessedPaymentRequest {

    private String referenceId;

    private Integer amountInCents;

    @NotBlank
    @Size(max = 35)
    private String description;

    private LocalDate expiryDate;

    @Id
    private String paymentRequestToken;

    private String url;

    private ZonedDateTime createdDateTime;

    private PaymentRequestStatus status;

    public ProcessedPaymentRequest(){

    }
    public ProcessedPaymentRequest(String paymentRequestToken) {
        this.paymentRequestToken = paymentRequestToken;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        String objId = ((ProcessedPaymentRequest) obj).paymentRequestToken;

        return objId==null? false:Objects.equals(paymentRequestToken,objId);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(paymentRequestToken);
    }
}
