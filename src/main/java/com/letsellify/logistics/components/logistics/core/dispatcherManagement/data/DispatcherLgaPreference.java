package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.LgaPreferenceEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.dataMapper.StateLGADataMapper;

import java.util.UUID;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record DispatcherLgaPreference(
        long id,
        UUID preferenceId,
        String pickUpLga,
        String dropOffLga
) {
    public DispatcherLgaPreference(LgaPreferenceEntity entity) {
        this(entity.getId(), entity.getPreferenceId(), entity.getPickUpLga(), entity.getDropOffLga());
    }

    public DispatcherLgaPreferenceResource getResource() {
        return DispatcherMapper.INSTANCE.dataToResource(this);
    }
}
