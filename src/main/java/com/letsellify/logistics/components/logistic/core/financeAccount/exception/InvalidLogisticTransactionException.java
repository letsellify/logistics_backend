package com.letsellify.logistics.components.logistic.core.financeAccount.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:09:00
 */

public class InvalidLogisticTransactionException extends LogisticsException {

    public InvalidLogisticTransactionException(final String msg) {
        super(msg);
    }

    public InvalidLogisticTransactionException(final String msg, final Object... args) {
        super(msg, args);
    }

    public InvalidLogisticTransactionException(final Exception cause) {
        super(cause);
    }

}
