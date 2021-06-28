package com.prasad.nithin.luminorgroup.event;

import com.prasad.nithin.luminorgroup.entity.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SepaCreatedEvent extends ApplicationEvent {

    private Payment paymentObj;

    public SepaCreatedEvent(Payment source) {
        super(source);
        this.paymentObj=source;
    }
}
