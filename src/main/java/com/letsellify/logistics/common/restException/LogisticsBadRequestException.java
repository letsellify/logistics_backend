package com.letsellify.logistics.common.restException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LogisticsBadRequestException extends LogisticsRestException {
    public LogisticsBadRequestException(final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, null, cause);
    }

    public LogisticsBadRequestException(final String msg) {
        super(HttpStatus.BAD_REQUEST, null, msg);
    }

    public LogisticsBadRequestException(final String msg, final Object... args) {
        super(HttpStatus.BAD_REQUEST, null, String.format(msg, args));
    }
}
