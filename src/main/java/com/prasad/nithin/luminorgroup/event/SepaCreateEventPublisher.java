package com.prasad.nithin.luminorgroup.event;

import com.prasad.nithin.luminorgroup.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SepaCreateEventPublisher{

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * Publish event.
     *
     * @param payment the payment
     */
    public void publishEvent(Payment payment){
            SepaCreatedEvent publishEvent=new SepaCreatedEvent(payment);
            applicationEventPublisher.publishEvent(publishEvent );
        }
    }