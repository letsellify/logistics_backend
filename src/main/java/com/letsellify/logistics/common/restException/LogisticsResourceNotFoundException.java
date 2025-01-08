package com.letsellify.logistics.common.restException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LogisticsResourceNotFoundException extends LogisticsRestException {
    public LogisticsResourceNotFoundException(final Throwable cause) {
        super(HttpStatus.NOT_FOUND, null, cause);
    }

    public LogisticsResourceNotFoundException(final String msg, final Object... args) {
        super(HttpStatus.NOT_FOUND, null, String.format(msg, args));
    }
}
