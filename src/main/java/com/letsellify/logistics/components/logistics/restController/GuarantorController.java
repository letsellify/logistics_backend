package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.guarantorManagement.GuarantorDataService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date: 8/18/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/guarantor")
@Tag(name = "Guarantors API", description = "APIs for Guarantor-related metadata")
public class GuarantorController {

    private final GuarantorDataService guarantorDataService;

    @Operation(summary = "Get all supported guarantor careers")
    @GetMapping("/careers")
    public List<String> getAllGuarantorCareers() {
        return this.guarantorDataService.getAllGuarantorCareers();
    }

    @Operation(summary = "Get all supported guarantor relationships")
    @GetMapping("/relationships")
    public List<String> getAllGuarantorRelationships() {
        return this.guarantorDataService.getAllGuarantorRelationships();
    }
}

