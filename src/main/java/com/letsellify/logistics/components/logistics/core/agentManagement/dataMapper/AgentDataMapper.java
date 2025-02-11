package com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:58
 */

@Mapper
public interface AgentDataMapper {
    AgentDataMapper INSTANCE = Mappers.getMapper(AgentDataMapper.class);


}
