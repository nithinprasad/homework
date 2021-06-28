package com.prasad.nithin.luminorgroup.model;


import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.validator.CurrecyRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrossBorderPayment implements PaymentsBuilder {

    private String paymentId;
    @Min(value = 0L)
    private Number amount;
    @CurrecyRule(currency = {Currency.EUR,Currency.USD})
    private Currency currency;
    @NotEmpty
    @Size(max = 70)
    private String debtorIban;
    @NotEmpty
    @Size(max = 70)
    private String creditorIban;
    @Size(max = 70)
    private String details;
    @NotEmpty
    @Size(max = 70)
    private String bic;

    @Override
    public Payment buildPayment() {

            return Payment.builder()
                    .amount(this.getAmount())
                    .currency(this.getCurrency().getCode())
                    .debtorIban(this.getDebtorIban())
                    .creditorIban(this.getCreditorIban())
                    .details(this.getDetails())
                    .bic(this.getBic())
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentType(PaymentProduct.SWIFT.getPaymentproduct())
                    .build();

    }

}
