package com.prasad.nithin.luminorgroup.service;


import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.PaymentStatus;
import com.prasad.nithin.luminorgroup.repository.PaymentRepository;
import com.prasad.nithin.luminorgroup.validator.PaymentCancelValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentCancelValidator paymentCancelValidator;
    @Autowired
    PaymentCancelFeeCalculatorService calculatorService;

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment  cancelPayment(String paymentProduct, String paymentId) throws CustomExcpetion {
       Payment payment=  this.getPaymentDetails(paymentProduct,paymentId);
       paymentCancelValidator.checkIfValid(payment);
       calculatorService.calculateFee(payment);
       return paymentRepository.saveAndFlush(payment);
    }

    public List<Payment> getAllPayments(Boolean withCancelled, BigDecimal amount) {
        List<PaymentStatus> paymentStatusList=new ArrayList<>();
        paymentStatusList.add(PaymentStatus.PENDING);
        paymentStatusList.add(PaymentStatus.PROCESSED);
        paymentStatusList.add(PaymentStatus.FAILED);
        if(withCancelled){
           paymentStatusList.add(PaymentStatus.CANCELLED);
        }
      return   Optional.ofNullable(amount).map( a->paymentRepository.findByPaymentStatusIn(paymentStatusList).stream().filter(each->this.comapreAmount(amount,each.getAmount())).collect(Collectors.toList())).orElseGet(()->paymentRepository.findByPaymentStatusIn(paymentStatusList));

    }

    private boolean comapreAmount(BigDecimal input,Number db){
        return input.compareTo(new BigDecimal(db.toString()))==0?true:false;
    }

    public Payment getPaymentDetails(String paymentProduct, String paymentId) throws CustomExcpetion {
        return paymentRepository.findByPaymentTypeAndPaymentId(paymentProduct,paymentId)
                .stream()
                .findFirst()
                .orElseThrow(()->new CustomExcpetion(CustomExcpetion.ExceptionTypes.ERROR_NOT_FOUND));
    }
}
