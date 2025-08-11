package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity;


import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Author: Ahmad Buba
 * Date: 8/5/25
 */

@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
public class ConditionEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    protected ConditionEntity() {
    }

    public ConditionEntity(String name) {
        super();
        this.name = name;
    }
}
