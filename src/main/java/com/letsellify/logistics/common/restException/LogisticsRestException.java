package com.letsellify.logistics.common.restException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public class LogisticsRestException extends RuntimeException {
    private final HttpStatus status;

    @Getter
    private final String errorCode;

    public LogisticsRestException(final HttpStatus status, final String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public LogisticsRestException(final HttpStatus status, final String errorCode, final String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public LogisticsRestException(final HttpStatus status, final String errorCode, final String message,
                                 final Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public LogisticsRestException(final HttpStatus status, final String errorCode, final Throwable cause) {
        super(cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public LogisticsRestException(final HttpStatus status, final String errorCode, final String message,
                                 final Throwable cause, final boolean enableSuppression,
                                 final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return this.status;
    }

}
