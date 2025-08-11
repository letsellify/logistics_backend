package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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
