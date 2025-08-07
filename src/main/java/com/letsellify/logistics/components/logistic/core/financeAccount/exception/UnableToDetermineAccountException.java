package com.letsellify.logistics.components.logistic.core.financeAccount.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

public class UnableToDetermineAccountException extends LogisticsException {
    public UnableToDetermineAccountException(String msg) {
        super(msg);
    }

    public UnableToDetermineAccountException(String msg, Object... args) {
        super(msg, args);
    }

    public UnableToDetermineAccountException(Exception cause) {
        super(cause);
    }
}
