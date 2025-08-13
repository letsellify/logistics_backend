package com.letsellify.logistics.components.logistics.core.vendorManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/13/25
 */

public class CompleteVendorProfileException extends LogisticsException {
    public CompleteVendorProfileException(String msg) {
        super(msg);
    }

    public CompleteVendorProfileException(String msg, Object... args) {
        super(msg, args);
    }

    public CompleteVendorProfileException(Exception cause) {
        super(cause);
    }
}
