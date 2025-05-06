package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:21
 */

@Data
public class StateLGAResource {
    @JsonProperty("homeState")
    private String name;
    @JsonProperty("homeLga's")
    private List<String> lgas;
}
