package com.letsellify.logistics.components.fileStorage.core.implementation.amazonS3.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/11/25
 * Time:12:58
 */

public class LogisticsS3IOException extends LogisticsException {

    public LogisticsS3IOException(final String msg) {
        super(msg);
    }

    public LogisticsS3IOException(final String msg, final Object... args) {
        super(msg, args);
    }

    public LogisticsS3IOException(final Exception cause) {
        super(cause);
    }

}
