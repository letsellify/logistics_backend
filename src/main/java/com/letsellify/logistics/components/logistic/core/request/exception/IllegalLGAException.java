package com.letsellify.logistics.components.logistic.core.request.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:15:29
 */

public class IllegalLGAException extends LogisticsException {

    public IllegalLGAException(final String msg) {
        super(msg);
    }

    public IllegalLGAException(final String msg, final Object... args) {
        super(msg, args);
    }

    public IllegalLGAException(final Exception cause) {
        super(cause);
    }

}
