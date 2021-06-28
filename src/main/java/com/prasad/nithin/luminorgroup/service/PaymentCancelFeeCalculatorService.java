package com.prasad.nithin.luminorgroup.service;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.PaymentProduct;
import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PaymentCancelFeeCalculatorService {

    public double calculateFee(Payment payment) throws CustomExcpetion {
      double calculatedFee=  calculateFee(payment.getCreatedDateTime(), PaymentProduct.findPaymentProduct(payment.getPaymentType()).getCoefficient());
      payment.setCancellationFee(calculatedFee);
      payment.setCancellationTime(LocalDateTime.now());
      payment.setPaymentStatus(PaymentStatus.CANCELLED);
      return calculatedFee;
    }

    private double calculateFee(LocalDateTime createdTime,double coefficient){
        return  createdTime.until(LocalDateTime.now(), ChronoUnit.HOURS)*coefficient;
    }

}
