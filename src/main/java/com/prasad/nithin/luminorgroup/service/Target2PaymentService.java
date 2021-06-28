package com.prasad.nithin.luminorgroup.service;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.event.Target2CreateEventPublisher;
import com.prasad.nithin.luminorgroup.model.Target2Payment;
import com.prasad.nithin.luminorgroup.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Target2PaymentService {

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    Target2CreateEventPublisher target2CreateEventPublisher;

    public Payment validateAndInitiatePayment(Target2Payment payment){
        Payment newPayment= paymentRepository.save(payment.buildPayment());
        target2CreateEventPublisher.publishEvent(newPayment);
        return  newPayment;
    }

}
