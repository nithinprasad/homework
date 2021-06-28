package com.prasad.nithin.luminorgroup.service;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.event.SepaCreateEventPublisher;
import com.prasad.nithin.luminorgroup.model.SepaPayment;
import com.prasad.nithin.luminorgroup.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SepaPaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SepaCreateEventPublisher sepaCreateEventPublisher;

    /**
     *
     * @param payment
     * @return
     */
    public Payment validateAndInitiatePayment(SepaPayment payment){
        Payment newPayment= paymentRepository.save(payment.buildPayment());
        sepaCreateEventPublisher.publishEvent(newPayment);
        return newPayment;
    }

}
