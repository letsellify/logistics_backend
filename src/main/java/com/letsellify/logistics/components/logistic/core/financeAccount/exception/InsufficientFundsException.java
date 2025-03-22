package com.letsellify.logistics.components.logistic.core.financeAccount.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/21/25
 * Time:12:27
 */

public class InsufficientFundsException extends LogisticsException {

    public InsufficientFundsException(final String msg) {
        super(msg);
    }

    public InsufficientFundsException(final String msg, final Object... args) {
        super(msg, args);
    }

    public InsufficientFundsException(final Exception cause) {
        super(cause);
    }

}
