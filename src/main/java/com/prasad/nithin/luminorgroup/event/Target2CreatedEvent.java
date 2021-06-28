package com.prasad.nithin.luminorgroup.event;

import com.prasad.nithin.luminorgroup.entity.Payment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class Target2CreatedEvent extends ApplicationEvent {

    private Payment paymentObj;

    public Target2CreatedEvent(Payment source) {
        super(source);
        this.paymentObj=source;
    }
}
