package com.letsellify.logistics.components.user.core.userManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:26
 */

public class UserUnAuthorizedException extends LogisticsException {

    public UserUnAuthorizedException(final String msg) {
        super(msg);
    }

    public UserUnAuthorizedException(final String msg, final Object... args) {
        super(msg, args);
    }

    public UserUnAuthorizedException(final Exception cause) {
        super(cause);
    }

}
