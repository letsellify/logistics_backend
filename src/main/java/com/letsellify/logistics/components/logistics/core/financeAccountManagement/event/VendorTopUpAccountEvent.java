package com.letsellify.logistics.components.logistics.core.financeAccountManagement.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/22/25
 * Time:11:22
 */

@Getter
public class VendorTopUpAccountEvent extends ApplicationEvent {
    private final UUID vendorId;
    private final BigDecimal currentBalance;

    public VendorTopUpAccountEvent(final UUID vendorId, final BigDecimal currentBalance) {
        super(vendorId);
        this.vendorId = vendorId;
        this.currentBalance = currentBalance;
    }

}
