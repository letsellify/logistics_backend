package com.letsellify.logistics.components.logistics.core.vendorManagement.data;


import com.letsellify.logistics.components.logistics.core.vendorManagement.dataMapper.VendorDataMapper;
import com.letsellify.logistics.components.logistics.core.vendorManagement.database.entity.VendorEntity;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.resource.VendorContactInformationResource;

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
