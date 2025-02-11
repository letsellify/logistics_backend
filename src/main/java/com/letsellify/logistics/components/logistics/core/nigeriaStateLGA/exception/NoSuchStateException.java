package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:41
 */

public class NoSuchStateException extends LogisticsException {

    public NoSuchStateException(final String msg) {
        super(msg);
    }

    public NoSuchStateException(final String msg, final Object... args) {
        super(msg, args);
    }

    public NoSuchStateException(final Exception cause) {
        super(cause);
    }

}
