package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity;


import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date: 8/22/25
 */

@Entity
@EqualsAndHashCode(callSuper = true)
@Getter
public class LgaPreferenceEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID preferenceId;

    private String pickUpLga;

    private String dropOffLga;

    @Setter
    @ManyToOne
    private DispatcherEntity dispatcher;

    protected LgaPreferenceEntity() {}

    public LgaPreferenceEntity(final String pickUpLga, final String dropOffLga) {
        super();
        this.preferenceId = UUID.randomUUID();
        this.pickUpLga = pickUpLga;
        this.dropOffLga = dropOffLga;
    }

}
