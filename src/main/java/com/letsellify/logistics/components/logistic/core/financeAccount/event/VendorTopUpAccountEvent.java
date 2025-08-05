package com.letsellify.logistics.components.logistic.core.financeAccount.event;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

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
