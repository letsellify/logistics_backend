package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.database.entity;

import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

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
