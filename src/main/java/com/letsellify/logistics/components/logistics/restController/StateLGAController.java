package com.letsellify.logistics.components.logistics.restController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.StateLGADataService;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.rest.resource.StatesResource;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/25/25
 * Time:12:58
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stateLGA")
@Tag(name = "Dispatcher Management API", description = "API's for managing dispatchers")
public class StateLGAController {
    private final StateLGADataService stateLGADataService;

    @GetMapping("/states")
    public StatesResource getStateLGAData() {
        return this.stateLGADataService.getStates();
    }

    @GetMapping
    public StateLGAResource getStateLGAData(@RequestParam(required = true) final String state) {
        return this.stateLGADataService.getStateLGA(state);
    }


}
