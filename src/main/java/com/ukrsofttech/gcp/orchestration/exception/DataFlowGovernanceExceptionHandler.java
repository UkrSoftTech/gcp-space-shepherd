package com.ukrsofttech.gcp.orchestration.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ConcurrentModificationException;
import java.util.Map;

@ControllerAdvice
public class DataFlowGovernanceExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ErrorAttributes errorAttributes;

    @ExceptionHandler({ConcurrentModificationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    protected Map<String, Object> handleConcurrentModificationException(WebRequest request) {
        return errorAttributes.getErrorAttributes(request, false);
    }
}
