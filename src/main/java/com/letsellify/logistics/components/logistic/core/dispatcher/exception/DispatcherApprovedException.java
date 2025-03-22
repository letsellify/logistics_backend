package com.letsellify.logistics.components.logistic.core.dispatcher.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:12:28
 */

public class DispatcherApprovedException extends LogisticsException {

    public DispatcherApprovedException(final String msg) {
        super(msg);
    }

    public DispatcherApprovedException(final String msg, final Object... args) {
        super(msg, args);
    }

    public DispatcherApprovedException(final Exception cause) {
        super(cause);
    }

}
