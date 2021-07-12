package com.gamary.accountantservice.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Data
public class Payment {
    @Id
    private String paymentToken;
    private String tikkieId;
    private String counterPartyName;
    private String counterPartyAccountNumber;
    private Integer amountInCents;
    private String description;
    private ZonedDateTime createdDateTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name="paymentRequestToken")
    private ProcessedPaymentRequest paymentRequest;

    private LocalDate ecreatedDate;

    public Payment(){

    }

    public Payment(String paymentToken, String tikkieId, String counterPartyName, String counterPartyAccountNumber, Integer amountInCents, String description, ZonedDateTime createdDateTime, @NotNull ProcessedPaymentRequest paymentRequest, LocalDate createdDate) {
        this.paymentToken = paymentToken;
        this.tikkieId = tikkieId;
        this.counterPartyName = counterPartyName;
        this.counterPartyAccountNumber = counterPartyAccountNumber;
        this.amountInCents = amountInCents;
        this.description = description;
        this.createdDateTime = createdDateTime;
        this.paymentRequest = paymentRequest;
        this.ecreatedDate = createdDate;
    }

    public Payment(String paymentToken, String tikkieId, String counterPartyName, String counterPartyAccountNumber,
                   Integer amountInCents, String description, String processedPaymentRequestToken,
                   LocalDate createdDate) {
        this.paymentToken = paymentToken;
        this.tikkieId = tikkieId;
        this.counterPartyName = counterPartyName;
        this.counterPartyAccountNumber = counterPartyAccountNumber;
        this.amountInCents = amountInCents;
        this.description = description;
        this.paymentRequest = new ProcessedPaymentRequest(processedPaymentRequestToken);
        this.ecreatedDate = createdDate;
    }

    public static Payment of(Integer amountInCents, String description, LocalDate createdDate, String counterPartyName, String counterPartyAccountNumber){
        return new Payment();
    }

    @PreUpdate
    @PrePersist
    public void calc() {
        ecreatedDate = createdDateTime.toLocalDate();
    }
}
