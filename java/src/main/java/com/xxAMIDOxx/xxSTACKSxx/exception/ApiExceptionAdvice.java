package com.xxAMIDOxx.xxSTACKSxx.exception;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.collect.Maps.newHashMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

@RestControllerAdvice
public class ApiExceptionAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        var validationIssues = processFieldErrors(ex.getBindingResult().getFieldErrors());
        ErrorResponse response = new ErrorResponse(
                ExceptionCode.VALIDATION_ERROR.getCode(),
                0,
                Objects.toString(request.getAttribute("CorrelationId", SCOPE_REQUEST)),
                "Invalid Request: " + validationIssues);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    private Map<String, String> processFieldErrors(List<FieldError> fieldErrors) {
        Map<String, String> errors = newHashMap();
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }

    @ResponseBody
    @ExceptionHandler(MenuAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse menuAlreadyExistsExceptionHandler(MenuAlreadyExistsException ex) {
        return new ErrorResponse(ex.exceptionCode.getCode(),
                ex.operationCode.getCode(),
                ex.correlationId,
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MenuNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse menuNotFoundExceptionHandler(MenuNotFoundException ex) {
        return new ErrorResponse(ex.exceptionCode.getCode(),
                ex.operationCode.getCode(),
                ex.correlationId,
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse categoryAlreadyExistsExceptionHandler(CategoryAlreadyExistsException ex) {
        return new ErrorResponse(ex.exceptionCode.getCode(),
                ex.operationCode.getCode(),
                ex.correlationId,
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(CategoryDoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse categoryNotFoundExceptionHandler(CategoryDoesNotExistException ex) {
        return new ErrorResponse(ex.exceptionCode.getCode(),
                ex.operationCode.getCode(),
                ex.correlationId,
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse itemAlreadyExistsExceptionHandler(ItemAlreadyExistsException ex) {
        return new ErrorResponse(ex.exceptionCode.getCode(),
                ex.operationCode.getCode(),
                ex.correlationId,
                ex.getMessage());
    }

}
