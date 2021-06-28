package com.prasad.nithin.luminorgroup.model;

import com.prasad.nithin.luminorgroup.exception.CustomExcpetion;
import lombok.Getter;

@Getter
public enum PaymentProduct {
    SEPA("sepa-credit-transfers",0.05),
    TARGET2("target-2-payments",0.1),
    SWIFT("cross-border-credit-transfers",0.15),
    ;

    private final String paymentproduct;
    private double coefficient;
    PaymentProduct(String paymentproduct, double coefficient) {
        this.paymentproduct=paymentproduct;
        this.coefficient=coefficient;
    }

    public static PaymentProduct findPaymentProduct(String paymentproduct) throws CustomExcpetion {
        for(PaymentProduct product:PaymentProduct.values()){
            if(product.getPaymentproduct().equals(paymentproduct)){
                return product;
            }
        }
        throw new CustomExcpetion(CustomExcpetion.ExceptionTypes.ERROR_NOT_FOUND);
    }


}
