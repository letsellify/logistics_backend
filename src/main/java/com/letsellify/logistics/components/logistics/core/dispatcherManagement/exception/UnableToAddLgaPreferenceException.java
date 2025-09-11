package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/22/25
 */

public class UnableToAddLgaPreferenceException extends LogisticsException {
    public UnableToAddLgaPreferenceException(String msg) {
        super(msg);
    }

    public UnableToAddLgaPreferenceException(String msg, Object... args) {
        super(msg, args);
    }

    public UnableToAddLgaPreferenceException(Exception cause) {
        super(cause);
    }
}
