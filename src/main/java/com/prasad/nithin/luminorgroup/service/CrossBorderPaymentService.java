package com.prasad.nithin.luminorgroup.service;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.model.CrossBorderPayment;
import com.prasad.nithin.luminorgroup.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrossBorderPaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    public Payment validateAndInitiatePayment(CrossBorderPayment payment){
        return paymentRepository.save(payment.buildPayment());
    }
}
