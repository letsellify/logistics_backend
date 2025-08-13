package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/12/25
 */

public class InCompleteDispatcherProfileException extends LogisticsException {
    public InCompleteDispatcherProfileException(String msg) {
        super(msg);
    }

    public InCompleteDispatcherProfileException(String msg, Object... args) {
        super(msg, args);
    }

    public InCompleteDispatcherProfileException(Exception cause) {
        super(cause);
    }
}
