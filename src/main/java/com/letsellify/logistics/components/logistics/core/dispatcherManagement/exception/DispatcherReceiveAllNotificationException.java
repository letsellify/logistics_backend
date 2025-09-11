package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/22/25
 */

public class DispatcherReceiveAllNotificationException extends LogisticsException {
    public DispatcherReceiveAllNotificationException(String msg) {
        super(msg);
    }

    public DispatcherReceiveAllNotificationException(String msg, Object... args) {
        super(msg, args);
    }

    public DispatcherReceiveAllNotificationException(Exception cause) {
        super(cause);
    }
}
