package com.letsellify.logistics.components.logistic.core.agent.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.agent.data.AgentInfo;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgent;
import com.letsellify.logistics.components.logistic.core.agent.data.LogisticAgentInfo;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.AgentPersonalInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentResource;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:58
 */

@Mapper
public interface AgentDataMapper {
    AgentDataMapper INSTANCE = Mappers.getMapper(AgentDataMapper.class);

    LogisticAgentResource toResource(LogisticAgent data);

    LogisticAgentInfoResource toResource(LogisticAgentInfo data);

    AgentPersonalInfoResource toResource(AgentInfo data);

}
