package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorBusinessInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorContactInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.data.VendorPersonalInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:32
 */


public record DispatcherInfo(
        String email,
        String profilePicture,
        DispatcherPersonalInformation personalInformation,
        DispatcherContactInformation contactInformation,
        DispatcherBusinessInformation businessInformation,
        DispatcherGuarantor guarantor,
        DispatcherKyc kyc
) {


    public DispatcherInfo(final String profilePicture, final DispatcherEntity dispatcherEntity) {
        this(
                dispatcherEntity.getEmail(),
                profilePicture,
                dispatcherEntity.getPersonalInformation() != null ? new DispatcherPersonalInformation(dispatcherEntity.getPersonalInformation()) : null,
                dispatcherEntity.getContactInformation() != null ? new DispatcherContactInformation(dispatcherEntity.getContactInformation()) : null,
                dispatcherEntity.getDispatchDetail() != null ? new DispatcherBusinessInformation(dispatcherEntity.getDispatchDetail()) : null,
                dispatcherEntity.getGuarantor() != null ? new DispatcherGuarantor(dispatcherEntity.getGuarantor()) : null,
                dispatcherEntity.getKycType() != null && dispatcherEntity.getKycNumber() != null ? new DispatcherKyc(dispatcherEntity.getKycType(), dispatcherEntity.getKycNumber()) : null
        );
    }

    public DispatcherProfileInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }

}
