package com.prasad.nithin.luminorgroup.validator;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Component
@NoArgsConstructor
public class PaymentCancelValidator {

    public boolean checkIfValid(Payment payment)  throws CustomExcpetion {

        if(PaymentStatus.CANCELLED.equals(payment.getPaymentStatus())){
            throw new CustomExcpetion("CANCELLED","Payment is already Cancelled", HttpStatus.BAD_REQUEST);
        }
       if(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).equals(payment.getCreatedDateTime().truncatedTo(ChronoUnit.DAYS))){
           return true;
        }else{
            throw new CustomExcpetion("DATE_EXPIRED","allowed time period expired", HttpStatus.BAD_REQUEST);
        }

    }
}
