package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.data;

import java.util.List;

import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.dataMapper.StateLGADataMapper;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource.StatesResource;

import lombok.Data;

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
