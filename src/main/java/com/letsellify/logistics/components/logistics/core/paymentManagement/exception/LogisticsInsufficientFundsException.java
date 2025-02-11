package com.letsellify.logistics.components.logistics.core.paymentManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/21/25
 * Time:12:27
 */

public class LogisticsInsufficientFundsException extends LogisticsException {

    public LogisticsInsufficientFundsException(final String msg) {
        super(msg);
    }

    public LogisticsInsufficientFundsException(final String msg, final Object... args) {
        super(msg, args);
    }

    public LogisticsInsufficientFundsException(final Exception cause) {
        super(cause);
    }

}
