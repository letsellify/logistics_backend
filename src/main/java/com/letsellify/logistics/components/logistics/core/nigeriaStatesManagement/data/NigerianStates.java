package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.data;

import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.dataMapper.StateLGADataMapper;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StatesResource;
import lombok.Data;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:17
 */

@Data
public class NigerianStates {
    private List<String> names;

    public NigerianStates(final List<String> stateNames) {
        this.names = stateNames;
    }

    public StatesResource getResource() {
        return StateLGADataMapper.INSTANCE.dateToResource(this);
    }
}
