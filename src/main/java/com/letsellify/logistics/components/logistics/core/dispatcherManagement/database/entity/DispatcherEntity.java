package com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:06
 */


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class DispatcherEntity extends Auditable {
    @Id
    private UUID id;

    @NaturalId
    @Column(unique = true)
    private String email;
    @Setter
    private PersonalInfoEmbeddable personalInformation;

    @Setter
    private String profileImage;

    @Setter
    private ContactInfoEmbeddable contactInformation;
    @Setter
    private DispatchDetailEmbeddable dispatchDetail;

    @Setter
    private DispatcherGuarantorEmbeddable guarantor;

    @Setter
    @Enumerated(EnumType.STRING)
    private KycType kycType;

    @Setter
    private String kycNumber;
//    private UUID kycId;
    private boolean currentlyAcceptingDelivery;
    private boolean approve;
    private boolean profileComplete;


    protected DispatcherEntity() {
        super();
    }

    public static DispatcherEntity getInstance(final @NonNull String email, final String name) {
        final DispatcherEntity dispatcherEntity = new DispatcherEntity();
        dispatcherEntity.id = UUID.randomUUID();
        dispatcherEntity.email = email;
        dispatcherEntity.personalInformation = new PersonalInfoEmbeddable(name);
        dispatcherEntity.approve = false;
        dispatcherEntity.currentlyAcceptingDelivery = false;
        return dispatcherEntity;
    }

    public void approve() {
        this.approve = true;
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
    public static class DispatchDetailEmbeddable {
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
    public static class DispatcherGuarantorEmbeddable {
        @Column(name = "guarantor_fullName")
        private String fullName;
        @Column(name = "guarantor_state")
        private String state;
        @Column(name = "guarantor_lga")
        private String lga;
        @Column(name = "guarantor_address")
        private String address;
        private String relationship;
        @Column(name = "guarantor_phoneNumber")
        private String phoneNumber;
        @Column(name = "guarantor_whatsAppNumber")
        private String whatsAppNumber;
        @Column(name = "guarantor_email")
        private String email;
        @Column(name = "guarantor_career")
        private String career;
    }
}
