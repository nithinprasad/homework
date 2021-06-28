package com.prasad.nithin.luminorgroup.controller;

import com.prasad.nithin.luminorgroup.entity.Payment;
import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import com.prasad.nithin.luminorgroup.model.CrossBorderPayment;
import com.prasad.nithin.luminorgroup.model.SepaPayment;
import com.prasad.nithin.luminorgroup.model.Target2Payment;
import com.prasad.nithin.luminorgroup.service.CrossBorderPaymentService;
import com.prasad.nithin.luminorgroup.service.PaymentService;
import com.prasad.nithin.luminorgroup.service.SepaPaymentService;
import com.prasad.nithin.luminorgroup.service.Target2PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    @Autowired
    SepaPaymentService sepaPaymentService;

    @Autowired
    Target2PaymentService target2PaymentService;

    @Autowired
    CrossBorderPaymentService crossBorderPaymentService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping(value = "sepa-credit-transfers" ,method = RequestMethod.POST)
    public ResponseEntity<Payment> createSepaPayment(@Valid @RequestBody SepaPayment sepaPayment){

        Payment payment= sepaPaymentService.validateAndInitiatePayment(sepaPayment);
        return new ResponseEntity<>(payment,HttpStatus.CREATED);
    }

    @RequestMapping(value = "{paymentProduct}/{paymentId}" ,method = RequestMethod.DELETE)
    public ResponseEntity<Payment> cancelSepaPayment(@PathVariable String paymentProduct, @PathVariable String paymentId) throws CustomExcpetion {

        return new ResponseEntity( paymentService.cancelPayment(paymentProduct,paymentId),HttpStatus.OK);
    }

    @RequestMapping(value = "target-2-payments" ,method = RequestMethod.POST )
    public  ResponseEntity<Payment> createTarget2Payment(@Valid @RequestBody Target2Payment target2Payment){
        Payment payment= target2PaymentService.validateAndInitiatePayment(target2Payment);
        return new ResponseEntity<Payment>(payment,HttpStatus.CREATED);
    }

    @RequestMapping(value = "cross-border-credit-transfers" ,method = RequestMethod.POST)
    public  ResponseEntity<Payment> createCrossBorderPayment(@Valid  @RequestBody CrossBorderPayment crossBorderPayment){
        Payment payment= crossBorderPaymentService.validateAndInitiatePayment(crossBorderPayment);
        return new ResponseEntity<>(payment,HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public  ResponseEntity getAllPayments(@RequestParam(required = false) boolean withCancelled,@RequestParam(required = false) BigDecimal amount){

        return new ResponseEntity<>( paymentService.getAllPayments(withCancelled,amount),HttpStatus.OK);
    }

    @RequestMapping(value = "{paymentProduct}/{paymentId}" ,method = RequestMethod.GET)
    public ResponseEntity<Payment> getPaymentDetails(@PathVariable String paymentProduct, @PathVariable String paymentId) throws CustomExcpetion {
        return new ResponseEntity( paymentService.getPaymentDetails(paymentProduct,paymentId),HttpStatus.OK);
    }


}
