package com.prasad.nithin.luminorgroup.validator;

import com.prasad.nithin.luminorgroup.model.Currency;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface CurrecyRule {
    String message() default "Not valid currency";
    Currency [] currency() default {};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

class CurrencyValidator implements ConstraintValidator<CurrecyRule,Currency>{

    List<Currency> currencyList;


    @Override
    public boolean isValid(Currency currency, ConstraintValidatorContext constraintValidatorContext) {
        if(currencyList.isEmpty()){
            return true;
        }
        return currencyList.contains(currency);
    }

    @Override
    public void initialize(CurrecyRule constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.currencyList= Optional.ofNullable(constraintAnnotation.currency()).map(Arrays::asList).orElse(Collections.emptyList());
    }
}