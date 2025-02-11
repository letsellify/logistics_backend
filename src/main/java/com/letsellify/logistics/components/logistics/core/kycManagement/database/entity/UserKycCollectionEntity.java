package com.letsellify.logistics.components.logistics.core.kycManagement.database.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.audit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticsAppRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:46
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class UserKycCollectionEntity extends Auditable {
    @Id
    private UUID id;

    @NaturalId
    private String userEmail;

    private LogisticsAppRole userType;

    @OneToMany(mappedBy = "userKycCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KycEntity> kycs = new ArrayList<>();

    private boolean approved;

    protected UserKycCollectionEntity() {
        super();
    }

    public static UserKycCollectionEntity getInstance(final String userEmail, final LogisticsAppRole userType) {
        final UserKycCollectionEntity kycEntity = new UserKycCollectionEntity();
        kycEntity.userEmail = userEmail;
        kycEntity.userType = userType;
        return kycEntity;
    }


    // this class should enforce kyc rules based on userType

    public void addKyc(final KycEntity kyc) {
        if (LogisticsAppRole.VENDOR.equals(this.userType)) {
            throw  new RuntimeException("You are not allowed to add a Vendor Kyc");
        }

        this.kycs.add(kyc);
    }


    public void removeKyc(final KycEntity kyc) {
        this.kycs.remove(kyc);
        kyc.setUserKycCollection(null);  // Maintain consistency
    }


}
