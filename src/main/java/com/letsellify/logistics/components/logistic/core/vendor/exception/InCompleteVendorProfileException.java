package com.letsellify.logistics.components.logistic.core.vendor.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/4/25
 */

public class InCompleteVendorProfileException extends LogisticsException {
    public InCompleteVendorProfileException(String msg) {
        super(msg);
    }

    public InCompleteVendorProfileException(String msg, Object... args) {
        super(msg, args);
    }

    public InCompleteVendorProfileException(Exception cause) {
        super(cause);
    }
}
