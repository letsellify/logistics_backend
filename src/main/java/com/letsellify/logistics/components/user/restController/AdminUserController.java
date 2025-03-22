package com.letsellify.logistics.components.user.restController;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentInfoResource;
import com.letsellify.logistics.components.logistic.core.agent.rest.resource.LogisticAgentResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistic.core.dispatcher.rest.resource.LogisticDispatcherResource;
import com.letsellify.logistics.components.user.core.logisticUser.AdminUserDataService;
import com.letsellify.logistics.components.user.core.logisticUser.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.logisticUser.rest.resource.UserResources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:13:29
 */


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin API", description = "API's for Admin User")
public class AdminUserController {
    private final AdminUserDataService dataService;

    @Operation(
      description = "Get a user",
      summary = "Gets a user by using the provided email to search"
    )
    @GetMapping("/users")
    public UserResource getUser(@RequestParam final @NonNull String email) {
        return this.dataService.getUser(email);
    }

    @Operation(
      description = "Get users"
    )
    @GetMapping("/users/all")
    public UserResources getAllUsers(
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "5") final int size,
      @RequestParam(defaultValue = "creationDate") final String sortBy,
      @RequestParam(defaultValue = "true") final boolean ascending
    ) {

        final Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return this.dataService.getAllUsers(pageable);
    }


    @GetMapping("/dispatchers/info")
    public LogisticDispatcherInfoResource viewDispatcherPersonalInfo(@RequestParam final String email) {
        return this.dataService.viewDispatcherPersonalInfo(email);
    }

    @PutMapping("/dispatchers/approve")
    public LogisticDispatcherResource approveDispatcher(@RequestParam final String email) {
        return this.dataService.approveDispatcher(email);
    }

    @GetMapping("/agents/info")
    public LogisticAgentInfoResource viewAgentPersonalInfo(@RequestParam final String email) {
        return this.dataService.viewAgentPersonalInfo(email);
    }

    @PutMapping("/agents/approve")
    public LogisticAgentResource approveAgent(@RequestParam final String email) {
        return this.dataService.approveAgent(email);
    }

}
