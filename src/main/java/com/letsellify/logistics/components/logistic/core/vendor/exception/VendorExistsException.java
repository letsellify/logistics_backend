package com.letsellify.logistics.components.logistic.core.vendor.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:18
 */

public class VendorExistsException extends LogisticsException {

    public VendorExistsException(final String msg) {
        super(msg);
    }

    public VendorExistsException(final String msg, final Object... args) {
        super(msg, args);
    }

    public VendorExistsException(final Exception cause) {
        super(cause);
    }

}
