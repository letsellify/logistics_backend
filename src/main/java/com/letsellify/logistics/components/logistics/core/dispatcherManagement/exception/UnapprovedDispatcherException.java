package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:12:16
 */

public class UnapprovedDispatcherException extends LogisticsException {

    public UnapprovedDispatcherException(final String msg) {
        super(msg);
    }

    public UnapprovedDispatcherException(final String msg, final Object... args) {
        super(msg, args);
    }

    public UnapprovedDispatcherException(final Exception cause) {
        super(cause);
    }

}
