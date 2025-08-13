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
        String name,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address,
        String businessName,
        String businessOfficeAddress,
        String businessState,
        String businessLga,
        String guarantorName,
        String guarantorState,
        String guarantorLga,
        String guarantorAddress,
        String guarantorRelationship,
        String guarantorPhoneNumber,
        String guarantorWhatsAppNumber,
        String guarantorEmail,
        String guarantorCareer,
        KycType kycType,
        String kycNumber
) {


    public DispatcherInfo(final DispatcherEntity dispatcherEntity) {
        this(
                dispatcherEntity.getEmail(),
                dispatcherEntity.getPersonalInfo().getName(),
                dispatcherEntity.getContactInfo().getWhatsAppPhone(),
                dispatcherEntity.getContactInfo().getPhone(),
                dispatcherEntity.getPersonalInfo().getState(),
                dispatcherEntity.getPersonalInfo().getLga(),
                dispatcherEntity.getPersonalInfo().getAddress(),
                dispatcherEntity.getDispatchDetail().getBusinessName(),
                dispatcherEntity.getDispatchDetail().getBusinessOfficeAddress(),
                dispatcherEntity.getDispatchDetail().getBusinessState(),
                dispatcherEntity.getDispatchDetail().getBusinessLga(),
                dispatcherEntity.getGuarantor().getFullName(),
                dispatcherEntity.getGuarantor().getState(),
                dispatcherEntity.getGuarantor().getLga(),
                dispatcherEntity.getGuarantor().getAddress(),
                dispatcherEntity.getGuarantor().getRelationship(),
                dispatcherEntity.getGuarantor().getPhoneNumber(),
                dispatcherEntity.getGuarantor().getWhatsAppNumber(),
                dispatcherEntity.getGuarantor().getEmail(),
                dispatcherEntity.getGuarantor().getCareer(),
                dispatcherEntity.getKycType(),
                dispatcherEntity.getKycNumber()

        );
    }

    public DispatcherProfileInfoResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
