package com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.agentManagement.data.Agent;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentInfo;
import com.letsellify.logistics.components.logistics.core.agentManagement.data.AgentProfileInfo;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentProfileInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;
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

    AgentResource toResource(Agent data);

    AgentInfoResource toResource(AgentInfo data);

    AgentProfileInfoResource toResource(AgentProfileInfo data);

}
