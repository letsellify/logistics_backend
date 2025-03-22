package com.letsellify.logistics.components.logistic.core.vendor.database.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:08
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class VendorEntity extends Auditable {
    @Id
    private UUID id;

    private String vendorName;
    private String email;
    private String phone;
    private BigDecimal currentAccountBalance;

    protected VendorEntity() {
        super();
    }

    public static VendorEntity getInstance(final String email) {
        final VendorEntity entity = new VendorEntity();
        entity.id = UUID.randomUUID();
        entity.email = email;
        entity.currentAccountBalance = BigDecimal.ZERO;
        return entity;
    }
}
