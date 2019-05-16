package com.fluance.demo;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingAdvice {
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
    
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
