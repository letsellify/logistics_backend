package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:32
 */

@Data
public class StatesResource {
    @JsonProperty("states")
    List<String> names;
}
