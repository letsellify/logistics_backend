package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.data;

import java.util.Set;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.dataMapper.StateLGADataMapper;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StateLGAResource;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:12:56
 */

@Data
public class NigerianStateLGA {
    private String name;
    private Set<String> lgas; // Directly using Set

    public NigerianStateLGA(final String stateName, final Set<String> lgas) {
        this.name = stateName;
        this.lgas = lgas;
    }

    public StateLGAResource getResource() {
        return StateLGADataMapper.INSTANCE.dataToResource(this);
    }
}
