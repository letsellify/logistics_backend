package com.letsellify.logistics.components.logistic.core.request.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:17:53
 */

public class InvalidLogisticItemImageException extends LogisticsException {

    public InvalidLogisticItemImageException(final String msg) {
        super(msg);
    }

    public InvalidLogisticItemImageException(final String msg, final Object... args) {
        super(msg, args);
    }

    public InvalidLogisticItemImageException(final Exception cause) {
        super(cause);
    }

}
