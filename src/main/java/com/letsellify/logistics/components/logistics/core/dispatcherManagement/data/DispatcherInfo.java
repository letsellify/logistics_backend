package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;

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
                new DispatcherPersonalInformation(dispatcherEntity.getPersonalInformation()),
                new DispatcherContactInformation(dispatcherEntity.getContactInformation()),
                new DispatcherBusinessInformation(dispatcherEntity.getDispatchDetail()),
                new DispatcherGuarantor(dispatcherEntity.getGuarantor()),
                new DispatcherKyc(dispatcherEntity.getKycType(), dispatcherEntity.getKycNumber())
        );
    }

    public DispatcherProfileInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
