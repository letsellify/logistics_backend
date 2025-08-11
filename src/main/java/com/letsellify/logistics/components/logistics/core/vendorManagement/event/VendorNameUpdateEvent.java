package com.letsellify.logistics.components.logistics.core.vendorManagement.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:20:11
 */

@Getter
public class VendorNameUpdateEvent extends ApplicationEvent {
    private final String vendorEmail;
    private final String vendorNameBeforeUpdate;
    private final String vendorNameAfterUpdate;

    public VendorNameUpdateEvent(final String vendorEmail, final String vendorNameBeforeUpdate, final String vendorNameAfterUpdate) {
        super(vendorEmail);
        this.vendorEmail = vendorEmail;
        this.vendorNameBeforeUpdate = vendorNameBeforeUpdate;
        this.vendorNameAfterUpdate = vendorNameAfterUpdate;
    }

}
