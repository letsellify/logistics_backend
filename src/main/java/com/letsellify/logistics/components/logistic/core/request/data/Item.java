package com.letsellify.logistics.components.logistic.core.request.data;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:31
 */


@Data
@AllArgsConstructor
@NoArgsConstructor // Add this
@Embeddable // Add this if missing
public class Item {
    @Column(name = "item_name")
    private String name;
    @Column(name = "item_quantity")
    private int quantity;
    @Column(name = "item_description")
    private String description;
    @Column(name = "item_fragility")
    private String fragility;
    private Set<String> condition;
    @Column(name = "item_weight")
    private int weight;
}
