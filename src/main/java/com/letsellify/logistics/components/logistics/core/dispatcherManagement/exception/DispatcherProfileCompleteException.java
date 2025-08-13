package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/12/25
 */

public class DispatcherProfileCompleteException extends LogisticsException {

    public DispatcherProfileCompleteException(String msg) {
        super(msg);
    }

    public DispatcherProfileCompleteException(String msg, Object... args) {
        super(msg, args);
    }

    public DispatcherProfileCompleteException(Exception cause) {
        super(cause);
    }
}
