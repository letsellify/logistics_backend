package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherContactInformationResource;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherContactInformation(
        String whatsAppPhone,
        String phone
) {
    public DispatcherContactInformation(DispatcherEntity.ContactInfoEmbeddable contactInfoEmbeddable) {
        this(contactInfoEmbeddable.getWhatsAppPhone(), contactInfoEmbeddable.getPhone());
    }

    public DispatcherContactInformationResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
