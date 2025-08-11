package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:16:37
 */

public class LogisticFraudException extends LogisticsException {

    public LogisticFraudException(final String msg) {
        super(msg);
    }

    public LogisticFraudException(final String msg, final Object... args) {
        super(msg, args);
    }

    public LogisticFraudException(final Exception cause) {
        super(cause);
    }

}
