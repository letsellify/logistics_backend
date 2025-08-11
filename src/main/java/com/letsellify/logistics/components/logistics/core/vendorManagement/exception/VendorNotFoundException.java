package com.letsellify.logistics.components.logistics.core.vendorManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/21/25
 * Time:10:46
 */

public class VendorNotFoundException extends LogisticsException {

    public VendorNotFoundException(final String msg) {
        super(msg);
    }

    public VendorNotFoundException(final String msg, final Object... args) {
        super(msg, args);
    }

    public VendorNotFoundException(final Exception cause) {
        super(cause);
    }

}
