package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesDataService;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StatesResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AHMAD BUBA
 * Date:1/25/25
 * Time:12:58
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stateLGA")
@Tag(name = "Nigerian State LGA API", description = "API's for Nigerian States and LGA's ")
public class StateLGAController {
    private final NigeriaStatesDataService nigeriaStatesDataService;

    @GetMapping("/states")
    public StatesResource getStateLGAData() {
        return this.nigeriaStatesDataService.getStates();
    }

    @GetMapping
    public StateLGAResource getStateLGAData(@RequestParam(required = true) final String state) {
        return this.nigeriaStatesDataService.getStateLGA(state);
    }


}
