package com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/26/25
 * Time:07:18
 */

public class FinanceAccountNotFoundException extends LogisticsException {

    public FinanceAccountNotFoundException(final String msg) {
        super(msg);
    }

    public FinanceAccountNotFoundException(final String msg, final Object... args) {
        super(msg, args);
    }

    public FinanceAccountNotFoundException(final Exception cause) {
        super(cause);
    }

}
