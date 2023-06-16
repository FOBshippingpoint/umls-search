package com.sscs.metamaplite;

import com.sscs.concept.ConceptNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MetaMapLiteProcessingExceptionHandler {
    @ResponseBody
    @ExceptionHandler(MetaMapLiteProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String metaMapLiteProcessingExceptionHandler(MetaMapLiteProcessingException ex) {
        return ex.getLocalizedMessage();
    }
}
