package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.audit.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:06
 */


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatcherEntity extends Auditable {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    @NaturalId
    @Column(unique = true)
    private String email;
    private String phone;
    private String address;
    private String nin;
    private String internationalPassport;
    private String bvn;
    private String utilityBill;
    private boolean currentlyAcceptingDelivery;
    private boolean enabled;


    protected DispatcherEntity() {
        super();
    }

    public static DispatcherEntity getInstance(final String email) {
        final DispatcherEntity dispatcherEntity = new DispatcherEntity();
        dispatcherEntity.id = UUID.randomUUID();
        dispatcherEntity.email = email;
        dispatcherEntity.enabled = true;
        return dispatcherEntity;
    }


}
