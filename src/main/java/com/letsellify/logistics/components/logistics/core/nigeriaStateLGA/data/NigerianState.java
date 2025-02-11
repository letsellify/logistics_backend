package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.data;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.dataMapper.StateLGADataMapper;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StateResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:14
 */

@Data
public class NigerianState {
    private String name;

    public NigerianState(final String stateName) {
        this.name = stateName;
    }

    public StateResource getStateResource() {
        return StateLGADataMapper.INSTANCE.dataToResource(this);
    }
}
