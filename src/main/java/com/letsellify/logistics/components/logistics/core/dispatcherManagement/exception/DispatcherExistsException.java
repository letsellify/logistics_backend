package com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;
import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:17:56
 */

@Getter
public class DispatcherExistsException extends LogisticsException {
    private final String message;

    public DispatcherExistsException(final String msg) {
        super(msg);
        this.message = msg;
    }

    public DispatcherExistsException(final String msg, final Object... args) {
        super(msg, args);
        this.message = msg;
    }

    public DispatcherExistsException(final Exception cause) {
        super(cause);
        this.message = cause.getMessage();
    }

}
