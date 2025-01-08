package com.letsellify.logistics.common.exception;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public abstract class LogisticsException extends Exception {
    public LogisticsException(final String msg) {
        super(msg);
    }

    public LogisticsException(final String msg, final Object... args) {
        super(String.format(msg, args));
    }

    public LogisticsException(final Exception cause) {
        super(cause);
    }
}
