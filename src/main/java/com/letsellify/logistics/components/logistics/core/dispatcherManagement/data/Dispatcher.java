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
                dispatcherEntity.getPersonalInformation() == null ? null : dispatcherEntity.getPersonalInformation().getName(),
                dispatcherEntity.getEmail(),
                dispatcherEntity.getContactInformation() == null ? null : dispatcherEntity.getContactInformation().getWhatsAppPhone(),
                dispatcherEntity.getContactInformation() == null ? null : dispatcherEntity.getContactInformation().getPhone(),
                dispatcherEntity.getPersonalInformation() == null ? null : dispatcherEntity.getPersonalInformation().getState(),
                dispatcherEntity.getPersonalInformation() == null ? null : dispatcherEntity.getPersonalInformation().getLga(),
                dispatcherEntity.getPersonalInformation() == null ? null : dispatcherEntity.getPersonalInformation().getAddress()
        );
    }

    public DispatcherResource getResource() {
        return DispatcherMapper.INSTANCE.getResource(this);
    }
}
