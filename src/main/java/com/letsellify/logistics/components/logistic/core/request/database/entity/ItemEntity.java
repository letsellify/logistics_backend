package com.letsellify.logistics.components.logistic.core.request.database.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "item_conditions",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id")
    )
    private Set<ConditionEntity> conditions = new HashSet<>();

    protected ItemEntity() {}

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
