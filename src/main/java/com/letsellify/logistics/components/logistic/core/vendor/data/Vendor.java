package com.letsellify.logistics.components.logistic.core.vendor.data;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.components.logistic.core.vendor.database.entity.VendorEntity;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:13
 */

@Data
public class Vendor {
    private final VendorEntity entity;

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String whatsAppPhone;
    private BigDecimal balance;

    public Vendor(final VendorEntity entity) {
        this.entity = entity;
        this.id = entity.getId();
        this.name = entity.getVendorName();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.whatsAppPhone = entity.getWhatsAppPhone();
        this.balance = entity.getCurrentAccountBalance();
    }
}
