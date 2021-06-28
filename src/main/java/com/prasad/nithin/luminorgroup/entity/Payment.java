package com.prasad.nithin.luminorgroup.entity;

import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String paymentId;
    private String paymentType;
    private Number amount;
    private String currency;
    private String debtorIban;
    private String creditorIban;
    private String bic;
    private String details;
    private String transactionId;
    private PaymentStatus paymentStatus;
    @CreationTimestamp
    private LocalDateTime createdDateTime;
    private Number cancellationFee;
    private LocalDateTime cancellationTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, paymentType, amount, currency, debtorIban, creditorIban, bic, details, transactionId, paymentStatus, createdDateTime, cancellationFee, cancellationTime);
    }
}
