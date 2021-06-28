package com.prasad.nithin.luminorgroup.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
@ToString
public class CustomExcpetion extends Exception{

    private HttpStatus status;
    private List<ErrorBody> errorBodies;

    public CustomExcpetion(HttpStatus status, List<ErrorBody> errorBodies) {
        super(status.toString());
        this.status = status;
        this.errorBodies = errorBodies;
    }

    public CustomExcpetion(String message, HttpStatus status, List<ErrorBody> errorBodies) {
        super(message);
        this.status = status;
        this.errorBodies = errorBodies;
    }

    public CustomExcpetion(String message, Throwable cause, HttpStatus status, List<ErrorBody> errorBodies) {
        super(message, cause);
        this.status = status;
        this.errorBodies = errorBodies;
    }

    public CustomExcpetion(Throwable cause, HttpStatus status, List<ErrorBody> errorBodies) {
        super(cause);
        this.status = status;
        this.errorBodies = errorBodies;
    }

    public CustomExcpetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status, List<ErrorBody> errorBodies) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
        this.errorBodies = errorBodies;
    }

    CustomExcpetion(ErrorBody errorBody, HttpStatus status){
        this(status,Arrays.asList(errorBody));
    }

    CustomExcpetion(String  errorMessage,HttpStatus status){
        this(status,Arrays.asList(new ErrorBody(errorMessage,errorMessage,null,null)));
    }

    public CustomExcpetion(String errorCode, String errorMessage, HttpStatus status){
        this(status,Arrays.asList(new ErrorBody(errorCode,errorMessage,null,null)));
    }

    public CustomExcpetion(ExceptionTypes type){
        this(type.getStatus(),Arrays.asList(new ErrorBody(type.getCode(),type.getMessage(),null,null)));
    }

    @Getter
   public enum ExceptionTypes{
         ERROR_NOT_FOUND("404","Resource Not Found",HttpStatus.NOT_FOUND);

         private String code;
         private String message;
        private HttpStatus status;
        ExceptionTypes(String code, String message, HttpStatus status) {
            this.code=code;
            this.message=message;
            this.status=status;
        }
    }


}
