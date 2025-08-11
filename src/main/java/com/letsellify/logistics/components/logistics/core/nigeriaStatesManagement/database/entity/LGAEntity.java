package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:36
 */

@Entity
@Table(name = "lga")
@Data
public class LGAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private StateEntity state;
}
