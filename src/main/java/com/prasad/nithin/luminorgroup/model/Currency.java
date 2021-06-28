package com.prasad.nithin.luminorgroup.model;

public enum Currency {

    EUR("EUR"),
    USD("USD")
    ;

    private final String code;

    Currency(String code){
        this.code=code;
    }

    public String getCode(){
        return this.code;
    }

}
