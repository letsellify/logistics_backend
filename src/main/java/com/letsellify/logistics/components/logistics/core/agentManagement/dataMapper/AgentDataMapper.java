package com.letsellify.logistics.components.logistics.core.agentManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.agentManagement.data.*;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.*;
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

    AgentProfileInfoResource toProfileResource(AgentInfo data);

    AgentProfileInfoResource toResource(AgentProfileInfo data);

    AgentPersonalInformationResource getResource(AgentPersonalInformation data);

    AgentKycResource getResource(AgentKyc data);

    AgentContactInformationResource toResource(AgentContactInformation data);

    AgentBusinessInformationResource toResource(AgentBusinessInformation data);

    AgentGuarantorResource toResource(AgentGuarantor data);

    AgentProfileInfoResources toProfileResources(AgentsInfo data);
}
