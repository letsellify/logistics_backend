package com.letsellify.logistics.components.logistics.core.user.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public class UserNotFoundException extends LogisticsException {

    public UserNotFoundException(final String msg) {
        super(msg);
    }

    public UserNotFoundException(final String msg, final Object... args) {
        super(msg, args);
    }

    public UserNotFoundException(final Exception cause) {
        super(cause);
    }

}
