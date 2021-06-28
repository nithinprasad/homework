package com.prasad.nithin.luminorgroup.builder;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.model.CrossBorderPayment;

public class PaymentsBuilder {

    public Payment createPaymentObject(CrossBorderPayment payment){
        return Payment.builder()
                .amount(payment.getAmount())
                .currency(payment.getCurrency().getCode())
                .debtorIban(payment.getDebtorIban())
                .creditorIban(payment.getCreditorIban())
                .details(payment.getDetails())
                .bic(payment.getBic())
                .build();
    }

}
