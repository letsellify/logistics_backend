package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:36
 */

@Entity
@Table(name = "state")
@Data
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY)
    private List<LGAEntity> lgas;
}
