package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.database.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:31
 */


@Entity
@Getter
@EqualsAndHashCode(callSuper = false)
public class ItemEntity {
    @Id
    private UUID id;

    private String name;
    private int quantity;
    private String description;
    private String fragility;
    private int weight;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "item_condition",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    private final Set<ConditionEntity> conditions = new HashSet<>();


    protected ItemEntity() {
    }

    public ItemEntity(String name, int quantity, String description, String fragility, int weight, Set<ConditionEntity> conditions) {
        super();
        this.id = UUID.randomUUID();
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.fragility = fragility;
        this.weight = weight;
        this.conditions.addAll(conditions);
    }
}
