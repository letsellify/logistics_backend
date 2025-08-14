package com.letsellify.logistics.components.user.restController;

import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Admin API", description = "Admin operations for managing users, vendors, dispatchers and agents")
public class AdminUserController {

    private final AdminUserDataService dataService;

    // ==============================
    // USERS
    // ==============================

    @Operation(
            summary = "Get user by email",
            description = "Retrieves details of a specific user using their email address.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @GetMapping("/users")
    public UserResource getUser(@RequestParam @NonNull String email) {
        return this.dataService.getUser(email);
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users.",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "5"),
                    @Parameter(name = "sortBy", description = "Field to sort by", example = "creationDate"),
                    @Parameter(name = "ascending", description = "Sort ascending if true, descending if false", example = "true")
            }
    )
    @GetMapping("/users/all")
    public UserResources getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.dataService.getAllUsers(pageable);
    }

    // ==============================
    // DISPATCHERS
    // ==============================

    @Operation(
            summary = "Get unapproved dispatchers",
            description = "Retrieves a paginated list of all dispatchers awaiting approval.",
            parameters = {
                    @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "5"),
                    @Parameter(name = "sortBy", description = "Field to sort by", example = "creationDate"),
                    @Parameter(name = "descending", description = "Sort descending if true, ascending if false", example = "true")
            }
    )
    @GetMapping("/dispatchers/unapproved")
    public DispatcherProfileInfoResources getAllDispatcherAwaitingApproval(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "creationDate") String sortBy,
            @RequestParam(defaultValue = "true") boolean descending
    ) {
        Sort sort = descending ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.dataService.getAllDispatcherAwaitingApproval(pageable);
    }

    @Operation(
            summary = "Approve dispatcher",
            description = "Approves a dispatcher account by their email address.",
            parameters = {
                    @Parameter(name = "email", description = "Email address of the dispatcher to approve", example = "dispatcher@example.com")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dispatcher approved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DispatcherResource.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Dispatcher not found")
            }
    )
    @PatchMapping("/dispatchers/{email}/approve")
    public DispatcherResource approveDispatcher(@PathVariable String email) {
        return this.dataService.approveDispatcher(email);
    }
}
