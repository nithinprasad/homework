package com.prasad.nithin.luminorgroup.event;

import com.prasad.nithin.luminorgroup.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Target2CreateEventPublisher{

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

        public void publishEvent(Payment payment){
            SepaCreatedEvent publishEvent=new SepaCreatedEvent(payment);
            applicationEventPublisher.publishEvent(publishEvent );
        }
    }