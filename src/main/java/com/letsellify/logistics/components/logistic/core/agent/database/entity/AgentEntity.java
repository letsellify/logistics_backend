package com.letsellify.logistics.components.logistic.core.agent.database.entity;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:21:09
 */

@Entity
@NoArgsConstructor
@Getter
public class AgentEntity extends Auditable {
    @Id
    private UUID id;
    private String name;
    @NaturalId
    @Column(unique = true)
    private String email;
    @Setter
    private PersonalInfoEmbeddable personalInfo;
    @Setter
    private UUID kycId;
    @Setter
    private boolean currentlyAcceptingStorage;
    private boolean approve;

    public AgentEntity(final @NonNull String email, final String name) {
        super();
        this.id = UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.currentlyAcceptingStorage = false;
        this.approve = false;
    }

    public void approve() {
        this.approve = true;
    }


    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class PersonalInfoEmbeddable {
        private String whatsAppPhone;
        private String phone;
        private String state;
        private String lga;
        private String address;
    }
}
