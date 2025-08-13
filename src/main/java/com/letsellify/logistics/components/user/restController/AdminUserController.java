package com.letsellify.logistics.components.user.restController;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.user.core.userManagement.AdminUserDataService;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResources;

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

    @GetMapping("/dispatchers/unapproved")
    public DispatcherProfileInfoResources getAllDispatcherAwaitingApproval(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "5") final int size,
            @RequestParam(defaultValue = "creationDate") final String sortBy,
            @RequestParam(defaultValue = "true") final boolean descending
    ) {
        final Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return this.dataService.getAllDispatcherAwaitingApproval(pageable);
    }

    @PatchMapping("/dispatchers/{email}/approve")
    public DispatcherResource approveDispatcher(@PathVariable final String email) {
        return this.dataService.approveDispatcher(email);
    }

    @GetMapping("/agents/info")
    public AgentInfoResource viewAgentPersonalInfo(@RequestParam final String email) {
        return this.dataService.viewAgentPersonalInfo(email);
    }

    @PutMapping("/agents/approve")
    public AgentResource approveAgent(@RequestParam final String email) {
        return this.dataService.approveAgent(email);
    }

}
