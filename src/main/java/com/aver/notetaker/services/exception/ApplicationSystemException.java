package com.aver.notetaker.services.exception;

import org.springframework.validation.BindingResult;

@SuppressWarnings("serial")
public class ApplicationSystemException extends ApplicationBaseException {

    public ApplicationSystemException(BindingResult fieldErrors) {
        super(fieldErrors);
    }

    public ApplicationSystemException(String code) {
        super(code);
    }

    public ApplicationSystemException(String code, Throwable cause) {
        super(code, cause);
    }
}
