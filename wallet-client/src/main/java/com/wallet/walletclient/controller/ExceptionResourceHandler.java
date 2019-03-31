package com.wallet.walletclient.controller;

import com.wallet.walletclient.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@ControllerAdvice
public class ExceptionResourceHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, InvalidArgumentsException.class, AmountShouldBeGreaterThanZeroException.class, InsufficientBalanceException.class,
            UnknowException.class})
    public final ResponseEntity<ExceptionResponse> handleCustomException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleUnknowExceptions(ExecutionException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Internal Error, please contact support",
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
