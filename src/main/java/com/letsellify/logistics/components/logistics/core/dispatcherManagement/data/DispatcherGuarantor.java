package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherGuarantorResource;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherGuarantor(
        String name,
        String state,
        String lga,
        String address,
        GuarantorRelationship relationship,
        String phoneNumber,
        String whatsAppNumber,
        String email,
        GuarantorCareer career
) {
    public DispatcherGuarantor(DispatcherEntity.DispatcherGuarantorEmbeddable dispatcherGuarantorEmbeddable) {
        this(dispatcherGuarantorEmbeddable.getFullName(), dispatcherGuarantorEmbeddable.getState(), dispatcherGuarantorEmbeddable.getLga(), dispatcherGuarantorEmbeddable.getAddress(), dispatcherGuarantorEmbeddable.getRelationship(), dispatcherGuarantorEmbeddable.getPhoneNumber(), dispatcherGuarantorEmbeddable.getWhatsAppNumber(), dispatcherGuarantorEmbeddable.getEmail(), dispatcherGuarantorEmbeddable.getCareer());
    }

    public DispatcherGuarantorResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
