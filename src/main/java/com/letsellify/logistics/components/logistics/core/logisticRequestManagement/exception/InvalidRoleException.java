package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/8/25
 */

public class InvalidRoleException extends LogisticsException {
    public InvalidRoleException(String msg) {
        super(msg);
    }

    public InvalidRoleException(String msg, Object... args) {
        super(msg, args);
    }

    public InvalidRoleException(Exception cause) {
        super(cause);
    }
}
