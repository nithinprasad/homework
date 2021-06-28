package com.prasad.nithin.luminorgroup.model;


import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.validator.CurrecyRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;

@Builder
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SepaPayment implements PaymentsBuilder {
    private String paymentId;
    @Min(value = 0L)
    @Digits(integer = 6,fraction = 2)
    private Number amount;
    @CurrecyRule(currency = {Currency.EUR})
    private Currency currency;
    @NotEmpty
    @Size(max = 70)
    private String debtorIban;
    @NotEmpty
    @Size(max = 70)
    private String creditorIban;
    @Size(max = 70)
    @NotEmpty
    private String details;


    @Override
    public Payment buildPayment() {
        return Payment.builder()
                .amount(this.getAmount())
                .currency(this.getCurrency().getCode())
                .debtorIban(this.getDebtorIban())
                .creditorIban(this.getCreditorIban())
                .debtorIban(this.getDebtorIban())
                .paymentType(PaymentProduct.SEPA.getPaymentproduct())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }
}
