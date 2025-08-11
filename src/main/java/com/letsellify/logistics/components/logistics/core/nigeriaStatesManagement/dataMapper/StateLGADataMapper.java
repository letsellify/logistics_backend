package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.data.NigerianState;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.data.NigerianStateLGA;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.data.NigerianStates;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StateResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StatesResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:24
 */

@Mapper
public interface StateLGADataMapper {
    StateLGADataMapper INSTANCE = Mappers.getMapper(StateLGADataMapper.class);

    StateResource dataToResource(NigerianState data);

    StateLGAResource dataToResource(NigerianStateLGA data);

    StatesResource dateToResource(NigerianStates data);


}
