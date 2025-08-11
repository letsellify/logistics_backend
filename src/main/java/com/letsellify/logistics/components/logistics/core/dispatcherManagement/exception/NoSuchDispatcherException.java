package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:32
 */

public class NoSuchDispatcherException extends LogisticsException {

    public NoSuchDispatcherException(final String msg) {
        super(msg);
    }

    public NoSuchDispatcherException(final String msg, final Object... args) {
        super(msg, args);
    }

    public NoSuchDispatcherException(final Exception cause) {
        super(cause);
    }

}
