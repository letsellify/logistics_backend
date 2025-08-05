package com.letsellify.logistics.components.logistic.core.vendor.data;


import com.letsellify.logistics.components.logistic.core.vendor.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistic.core.vendor.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorContactInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/5/25
 */


public record VendorContactInformation(
        String phoneNumber,
        String whatsAppPhoneNumber
) {
    public VendorContactInformation(VendorEntity.ContactInformationEmbeddable contactInformationEmbeddable) {
        this(contactInformationEmbeddable.getPhoneNumber(), contactInformationEmbeddable.getWhatsAppPhoneNumber());
    }

    public VendorContactInformationResource getResource() {
        return VendorDataMapper.INSTANCE.toResource(this);
    }
}
