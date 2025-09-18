package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Ahmad Buba
 * Date:8/19/25
 */

public record AgentContactInformationResource(@JsonProperty(value = "phoneNumber") String phone, @JsonProperty(value = "whatsAppPhoneNumber") String whatsAppPhone) {}
