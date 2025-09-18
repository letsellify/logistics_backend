package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */

public record DispatcherContactInformationResource(@JsonProperty(value = "phoneNumber") String phone, @JsonProperty(value = "whatsAppPhoneNumber") String whatsAppPhone) {}
