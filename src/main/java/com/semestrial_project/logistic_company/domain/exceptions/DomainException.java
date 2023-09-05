package com.semestrial_project.logistic_company.domain.exceptions;

import com.semestrial_project.logistic_company.domain.exceptions.code.ErrorCode;

public class DomainException extends Exception {
    private ErrorCode errorCode;
    private String customMessage;

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DomainException(ErrorCode errorCode, String customMessage) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = customMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}
