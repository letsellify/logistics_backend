package com.letsellify.logistics.components.logistics.core.agentManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

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

    @NaturalId
    @Column(unique = true)
    private String email;
    @Setter
    private PersonalInfoEmbeddable personalInfo;
    @Setter
    private ContactInfoEmbeddable contactInfo;
    @Setter
    private StoreDetailEmbeddable storeDetail;
    @Setter
    private UUID kycId;
    @Setter
    private boolean acceptingStorage;
    private boolean approved;

    public AgentEntity(final @NonNull String email, final String name) {
        super();
        this.id = UUID.randomUUID();
        this.email = email;
        this.personalInfo = new PersonalInfoEmbeddable(name);
        this.acceptingStorage = false;
        this.approved = false;
    }

    public void approve() {
        this.approved = true;
    }


    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class PersonalInfoEmbeddable {
        private String name;
        private String homeState;
        private String homeLga;
        private String homeAddress;

        public PersonalInfoEmbeddable(final String name) {
            this.name = name;
        }

    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class ContactInfoEmbeddable {
        private String whatsAppPhone;
        private String phone;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class StoreDetailEmbeddable {
        private String storeState;
        private String storeLga;
        private String storeAddress;
    }
}
