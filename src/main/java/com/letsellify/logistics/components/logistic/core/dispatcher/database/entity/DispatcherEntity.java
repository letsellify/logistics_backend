package com.letsellify.logistics.components.logistic.core.dispatcher.database.entity;

import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

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
    private String name;
    @NaturalId
    @Column(unique = true)
    private String email;
    private PersonalInfoEmbeddable personalInfo;
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
        dispatcherEntity.name = name;
        dispatcherEntity.approve = false;
        dispatcherEntity.currentlyAcceptingDelivery = false;
        return dispatcherEntity;
    }

    public void approve() {
        this.approve = true;
    }


    @Embeddable
    @Data
    public static class PersonalInfoEmbeddable {
        private String whatsAppPhone;
        private String phone;
        private String state;
        private String lga;
        private String address;


        // Constructor with parameters
        public PersonalInfoEmbeddable(final String whatsAppPhone, final String phone, final String state, final String lga, final String address) {
            this.whatsAppPhone = whatsAppPhone;
            this.phone = phone;
            this.state = state;
            this.lga = lga;
            this.address = address;
        }

        // No-arg constructor for JPA (required)
        protected PersonalInfoEmbeddable() {
            // JPA requires a no-arg constructor, but it can be protected
        }

    }

}
