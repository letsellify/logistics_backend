package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/23/25
 * Time:08:39
 */

public class NoSuchLogisticRequestException extends LogisticsException {

    public NoSuchLogisticRequestException(final String msg) {
        super(msg);
    }

    public NoSuchLogisticRequestException(final String msg, final Object... args) {
        super(msg, args);
    }

    public NoSuchLogisticRequestException(final Exception cause) {
        super(cause);
    }

}
