package com.letsellify.logistics.components.logistics.core.agentManagement.rest.dto;

/**
 * @author AHMAD BUBA
 * Date:2/27/25
 * Time:11:30
 */

public record AgentInfoDto(
        AgentPersonalInfoDto personalInfo,
        AgentContactInfoDto contactInfo,
        StoreDetailDto storeDetail
) {
}

