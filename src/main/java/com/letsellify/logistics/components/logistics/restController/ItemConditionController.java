package com.letsellify.logistics.components.logistics.restController;


import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.ItemConditionDataService;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemConditionResources;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Ahmad Buba
 * Date: 8/7/25
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item-condition")
@Tag(name = "Items Condition API", description = "API for Item conditions")
public class ItemConditionController {
    private final ItemConditionDataService itemConditionDataService;

    @GetMapping
    public LogisticItemConditionResources getItemConditions(final Authentication authentication) {
        return this.itemConditionDataService.getItemConditions();
    }
}
