package com.fluance.demo;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class TooManyItemsAdvice {
    @ResponseBody
    @ExceptionHandler(TooManyItemsException.class)
    @ResponseStatus(BAD_REQUEST)
    String handlerTooManyItemsException(TooManyItemsException ex) {
        return ex.getLocalizedMessage();
    }

    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    String handleItemNotFound(ItemNotFoundException ex) {
        return ex.getLocalizedMessage();
    }
}
