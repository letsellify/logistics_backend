package com.letsellify.logistics.components.logistics.core.user.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

public class UserExistsException extends LogisticsException {

    public UserExistsException(final String msg) {
        super(msg);
    }

    public UserExistsException(final String msg, final Object... args) {
        super(msg, args);
    }

    public UserExistsException(final Exception cause) {
        super(cause);
    }

}
