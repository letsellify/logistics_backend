package com.letsellify.logistics.components.logistics.core.kycManagement.database.entity;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private LogisticAppRole userType;

    @OneToMany(mappedBy = "userKycCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KycEntity> kycs = new ArrayList<>();

    private boolean approved = false;

    protected UserKycCollectionEntity() {
        super();
    }

    public static UserKycCollectionEntity getInstance(final String userEmail, final LogisticAppRole userType) {
        final UserKycCollectionEntity kycEntity = new UserKycCollectionEntity();
        kycEntity.id = UUID.randomUUID();
        kycEntity.userEmail = userEmail;
        kycEntity.userType = userType;
        return kycEntity;
    }


    // this class should enforce type rules based on userType

    public void addKyc(final KycEntity kyc) {
        if (LogisticAppRole.VENDOR.equals(this.userType)) {
            throw new RuntimeException("You are not allowed to add a Vendor Kyc");
        }
        kyc.setUserKycCollection(this);
        this.kycs.add(kyc);
    }


    public void removeKyc(final KycEntity kyc) {
        this.kycs.remove(kyc);
        kyc.setUserKycCollection(null);  // Maintain consistency
    }


}
