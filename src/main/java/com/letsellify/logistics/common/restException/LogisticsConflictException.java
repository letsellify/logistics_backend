package com.letsellify.logistics.common.restException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@ResponseStatus(value = HttpStatus.CONFLICT)
public class LogisticsConflictException extends LogisticsRestException {
    public LogisticsConflictException(final String errorCode, final Throwable cause) {
        super(HttpStatus.CONFLICT, errorCode, cause);
    }

    public LogisticsConflictException(final String msg, final String errorCode, final Object... args) {
        super(HttpStatus.CONFLICT, errorCode, String.format(msg, args));
    }
}
