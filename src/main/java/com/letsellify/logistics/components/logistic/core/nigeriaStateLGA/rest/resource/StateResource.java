package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:19
 */

@Data
public class StateResource {
    @JsonProperty("homeState")
    private String name;
}
