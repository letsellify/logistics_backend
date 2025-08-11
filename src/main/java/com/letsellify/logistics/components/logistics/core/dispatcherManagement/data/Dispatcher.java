package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:29
 */


public record Dispatcher(
        UUID id,
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {

    public Dispatcher(final DispatcherEntity dispatcherEntity) {
        this(
                dispatcherEntity.getId(),
                dispatcherEntity.getPersonalInfo()
                        .getName(),
                dispatcherEntity.getEmail(),
                dispatcherEntity.getContactInfo().getWhatsAppPhone(),
                dispatcherEntity.getContactInfo().getPhone(),
                dispatcherEntity.getPersonalInfo().getHomeState(),
                dispatcherEntity.getPersonalInfo().getHomeLga(),
                dispatcherEntity.getPersonalInfo().getHomeAddress()
        );
    }

    public DispatcherResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
