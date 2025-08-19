package com.letsellify.logistics.components.logistics.core.agentManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;
import jakarta.persistence.*;
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
    private String profileImage;

    @Setter
    private PersonalInfoEmbeddable personalInformation;
    @Setter
    private ContactInfoEmbeddable contactInformation;
    @Setter
    private StoreDetailEmbeddable businessInformation;
    @Setter
    private AgentGuarantorEmbeddable guarantor;

    @Setter
    @Enumerated(EnumType.STRING)
    private KycType kycType;

    @Setter
    private String kycNumber;

    @Setter
    private boolean acceptingStorage;
    @Setter
    private boolean approved;

    @Setter
    private boolean profileComplete;

    public AgentEntity(final @NonNull String email, final String name) {
        super();
        this.id = UUID.randomUUID();
        this.email = email;
        this.personalInformation = new PersonalInfoEmbeddable(name);
        this.approved = false;
        this.acceptingStorage = false;
        this.profileComplete = false;
    }

    public void approve() {
        this.approved = true;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Setter
    @Getter
    public static class PersonalInfoEmbeddable {
        private String name;
        private String state;
        private String lga;
        private String address;

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
        private String businessName;
        private String businessOfficeAddress;
        private String businessState;
        private String businessLga;
    }

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class AgentGuarantorEmbeddable {
        @Column(name = "guarantor_fullName")
        private String fullName;
        @Column(name = "guarantor_state")
        private String state;
        @Column(name = "guarantor_lga")
        private String lga;
        @Column(name = "guarantor_address")
        private String address;
        @Enumerated(EnumType.STRING)
        private GuarantorRelationship relationship;
        @Column(name = "guarantor_phoneNumber")
        private String phoneNumber;
        @Column(name = "guarantor_whatsAppNumber")
        private String whatsAppNumber;
        @Column(name = "guarantor_email")
        private String email;
        @Column(name = "guarantor_career")
        @Enumerated(EnumType.STRING)
        private GuarantorCareer career;

    }



}
