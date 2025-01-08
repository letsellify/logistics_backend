package com.letsellify.logistics.common.restException;

import org.springframework.http.HttpStatus;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public class LogisticsInternalServerErrorException extends LogisticsRestException {
    public LogisticsInternalServerErrorException(final Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, null, cause);
    }

    public LogisticsInternalServerErrorException(final String msg) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, null, msg);
    }
}
