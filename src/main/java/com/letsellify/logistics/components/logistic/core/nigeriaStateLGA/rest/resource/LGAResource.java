package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:20
 */

@Data
public class LGAResource {
    @JsonProperty("lga")
    private String name;
}
