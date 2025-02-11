package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.data.NigerianState;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.data.NigerianStateLGA;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.data.NigerianStates;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StateResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StatesResource;

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
