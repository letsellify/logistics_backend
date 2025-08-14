package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.NigeriaStatesDataService;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StatesResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Get all Nigerian states",
            description = "Returns a list of all Nigerian states.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StatesResource.class))
                    )
            }
    )
    @GetMapping
    public StatesResource getStates() {
        return this.nigeriaStatesDataService.getStates();
    }


    @Operation(
            summary = "Get LGAs by state name",
            description = "Fetches all LGAs for the specified state.",
            parameters = {
                    @Parameter(name = "state", description = "The name of the Nigerian state", example = "Lagos")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful retrieval",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StateLGAResource.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "State not found"
                    )
            }
    )
    @GetMapping("/{state}/lgas")
    public StateLGAResource getLgasByState(@PathVariable final @NonNull String state) {
        return this.nigeriaStatesDataService.getStateLGA(state);
    }

}
