package com.prasad.nithin.luminorgroup.event;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import com.prasad.nithin.luminorgroup.repository.PaymentRepository;
import com.prasad.nithin.luminorgroup.rest.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@Slf4j
public class SepaEventHandler {

    @Autowired
    PaymentRepository paymentRepository;

    @EventListener
    @Async
    public void handleEvent(SepaCreatedEvent sepaCreatedEvent) throws CustomExcpetion {
        ResponseEntity<String> responseEntity = RestClient.SepaRestCLient.notifySepaCreate(sepaCreatedEvent.getPaymentObj());
        log.info("HTTP Response{}",responseEntity);
        PaymentStatus paymentStatus =HttpStatus.OK.equals(responseEntity.getStatusCode())?PaymentStatus.PROCESSED:PaymentStatus.FAILED;
        Optional<Payment> optionalPayment=paymentRepository.findById(sepaCreatedEvent.getPaymentObj().getPaymentId());
        if(optionalPayment.isPresent()){
           Payment payment= optionalPayment.get();
           payment.setPaymentStatus(paymentStatus);
           paymentRepository.saveAndFlush(payment);
        }else{
            throw new CustomExcpetion(CustomExcpetion.ExceptionTypes.ERROR_NOT_FOUND);
        }
    }
}
