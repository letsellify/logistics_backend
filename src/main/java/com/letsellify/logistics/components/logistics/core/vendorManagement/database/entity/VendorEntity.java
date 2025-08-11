package com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:08
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class VendorEntity extends Auditable {
    @Id
    private UUID id;

    private String email;

    private String profilePicture;

    @Embedded
    private PersonalInformationEmbeddable personalInformation;
    @Embedded
    private ContactInformationEmbeddable contactInformation;

    @Embedded
    private BusinessInformationEmbeddable businessInformation;


    private BigDecimal currentAccountBalance;

    protected VendorEntity() {
        super();
    }

    public static VendorEntity getInstance(final String email, final String name) {
        final VendorEntity entity = new VendorEntity();
        entity.id = UUID.randomUUID();
        entity.email = email;
        entity.currentAccountBalance = BigDecimal.ZERO;
        return entity;
    }

    @Embeddable
    @Getter
    public static class PersonalInformationEmbeddable {
        private String name;
        private String homeAddress;
        @Column(name = "vendor_state")
        private String state;
        @Column(name = "vendor_lg")
        private String lg;

        public PersonalInformationEmbeddable() {
        }

        public PersonalInformationEmbeddable(@NonNull final String name) {
            this.name = name;
        }

        public PersonalInformationEmbeddable(@NonNull final String name, @NonNull final String homeAddress, @NonNull final String state, @NonNull final String lg) {
            this.name = name;
            this.homeAddress = homeAddress;
            this.state = state;
            this.lg = lg;
        }
    }

    @Embeddable
    @Getter
    public static class ContactInformationEmbeddable {
        private String phoneNumber;
        private String whatsAppPhoneNumber;

        public ContactInformationEmbeddable() {
        }

        public ContactInformationEmbeddable(@NonNull final String phoneNumber, @NonNull final String whatsAppPhoneNumber) {
            this.phoneNumber = phoneNumber;
            this.whatsAppPhoneNumber = whatsAppPhoneNumber;
        }
    }

    @Embeddable
    @Getter
    public static class BusinessInformationEmbeddable {
        private String businessName;
        private String businessOfficeAddress;
        @Column(name = "vendor_business_state")
        private String state;
        @Column(name = "vendor_business_lg")
        private String lg;

        public BusinessInformationEmbeddable() {
        }

        public BusinessInformationEmbeddable(@NonNull final String businessName, @NonNull final String businessOfficeAddress, @NonNull final String state, @NonNull final String lg) {
            this.businessName = businessName;
            this.businessOfficeAddress = businessOfficeAddress;
            this.state = state;
            this.lg = lg;
        }
    }
}
