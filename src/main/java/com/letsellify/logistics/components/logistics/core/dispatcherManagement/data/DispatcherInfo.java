package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorBusinessInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorContactInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorPersonalInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:32
 */


public record DispatcherInfo(
        UUID id,
        String email,
        String profilePicture,
        DispatcherPersonalInformation personalInformation,
        DispatcherContactInformation contactInformation,
        DispatcherBusinessInformation businessInformation,
        DispatcherGuarantor guarantor,
        DispatcherKyc kyc,
        boolean receiveAllNotifications,
        List<DispatcherLgaPreference> preferences,
        boolean currentlyAcceptingDelivery,
        boolean approve,
        boolean profileComplete
) {


    public DispatcherInfo(final String profilePicture, final DispatcherEntity dispatcherEntity) {
        this(
                dispatcherEntity.getId(),
                dispatcherEntity.getEmail(),
                profilePicture,
                dispatcherEntity.getPersonalInformation() != null ? new DispatcherPersonalInformation(dispatcherEntity.getPersonalInformation()) : null,
                dispatcherEntity.getContactInformation() != null ? new DispatcherContactInformation(dispatcherEntity.getContactInformation()) : null,
                dispatcherEntity.getDispatchDetail() != null ? new DispatcherBusinessInformation(dispatcherEntity.getDispatchDetail()) : null,
                dispatcherEntity.getGuarantor() != null ? new DispatcherGuarantor(dispatcherEntity.getGuarantor()) : null,
                dispatcherEntity.getKycType() != null && dispatcherEntity.getKycNumber() != null ? new DispatcherKyc(dispatcherEntity.getKycType(), dispatcherEntity.getKycNumber()) : null,
                dispatcherEntity.isReceiveAllNotifications(),
                dispatcherEntity.getPreferences() == null || dispatcherEntity.getPreferences().isEmpty() ? List.of(): dispatcherEntity.getPreferences().stream().map(DispatcherLgaPreference::new).toList(),
                dispatcherEntity.isCurrentlyAcceptingDelivery(),
                dispatcherEntity.isApprove(),
                dispatcherEntity.isProfileComplete()

        );
    }

    public DispatcherProfileInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }

}
