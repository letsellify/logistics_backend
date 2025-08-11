package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/8/25
 */

public class LogisticRequestAccessDeniedException extends LogisticsException {
    public LogisticRequestAccessDeniedException(String msg) {
        super(msg);
    }

    public LogisticRequestAccessDeniedException(String msg, Object... args) {
        super(msg, args);
    }

    public LogisticRequestAccessDeniedException(Exception cause) {
        super(cause);
    }
}
