package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/22/25
 */

public class NoSuchDispatcherPreferenceException extends LogisticsException {
    public NoSuchDispatcherPreferenceException(String msg) {
        super(msg);
    }

    public NoSuchDispatcherPreferenceException(String msg, Object... args) {
        super(msg, args);
    }

    public NoSuchDispatcherPreferenceException(Exception cause) {
        super(cause);
    }
}
