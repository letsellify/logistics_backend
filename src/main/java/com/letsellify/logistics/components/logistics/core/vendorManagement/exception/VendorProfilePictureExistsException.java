package com.letsellify.logistics.components.logistics.core.vendorManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/13/25
 */

public class VendorProfilePictureExistsException extends LogisticsException {
    public VendorProfilePictureExistsException(String msg) {
        super(msg);
    }

    public VendorProfilePictureExistsException(String msg, Object... args) {
        super(msg, args);
    }

    public VendorProfilePictureExistsException(Exception cause) {
        super(cause);
    }
}
