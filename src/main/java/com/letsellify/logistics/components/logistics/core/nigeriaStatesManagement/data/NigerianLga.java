package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.data;


import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.dataMapper.StateLGADataMapper;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.entity.LGAEntity;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.LgaResource;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record NigerianLga(
        String lga
) {
    public NigerianLga(LGAEntity entity) {
        this(entity.getName());
    }

    public LgaResource getResource() {
        return StateLGADataMapper.INSTANCE.dataToResource(this);
    }
}
