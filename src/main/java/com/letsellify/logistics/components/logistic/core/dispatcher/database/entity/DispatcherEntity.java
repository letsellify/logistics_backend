package com.letsellify.logistics.components.logistic.core.dispatcher.database.entity;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
    private PersonalInfoEmbeddable personalInfo;
    @Setter
    private ContactInfoEmbeddable contactInfo;
    @Setter
    private DispatchDetailEmbeddable dispatchDetail;
    private UUID kycId;
    private boolean currentlyAcceptingDelivery;
    private boolean approve;


    protected DispatcherEntity() {
        super();
    }

    public static DispatcherEntity getInstance(final @NonNull String email, final String name) {
        final DispatcherEntity dispatcherEntity = new DispatcherEntity();
        dispatcherEntity.id = UUID.randomUUID();
        dispatcherEntity.email = email;
        dispatcherEntity.personalInfo = new PersonalInfoEmbeddable(name);
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
    public static class DispatchDetailEmbeddable {
        private String identificationNumber;
        private String stateOfDispatch;
        private String stateOfLga;
    }
}
