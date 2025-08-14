package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.BankAccountDataService;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.dto.ResolveBankAccountDto;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.dto.UploadBankAccountDto;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankAccountResource;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.BankResources;
import com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.rest.resource.ResolvedBankAccountResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author AHMAD BUBA
 * Date:2/24/25
 * Time:16:04
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank")
@Tag(
        name = "Banking API",
        description = "Operations for managing bank details and resolving bank accounts within the Nigerian banking system."
)
public class BankController {

    private final BankAccountDataService bankDataService;

    @Operation(
            summary = "Get all banks in Nigeria",
            description = "Fetches a list of all banks in Nigeria, including their name, code, and type.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of all Nigerian banks",
                            content = @Content(
                                    schema = @Schema(implementation = BankResources.class),
                                    examples = @ExampleObject(
                                            name = "Banks List Example",
                                            value = """
                                                    {
                                                      "banks": [
                                                        { "name": "Access Bank", "code": "044", "type": "commercial" },
                                                        { "name": "GTBank", "code": "058", "type": "commercial" }
                                                      ],
                                                      "total_number_of_banks": 2
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/all")
    public BankResources getAllBanks() {
        return this.bankDataService.getBanks();
    }

    @Operation(
            summary = "Resolve bank account",
            description = "Resolves a Nigerian bank account by validating the account number and bank code, returning the associated account name.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Bank account details for resolution",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResolveBankAccountDto.class),
                            examples = @ExampleObject(
                                    name = "Resolve Account Example",
                                    value = """
                                            {
                                              "account_number": "01234567890",
                                              "bank_code": "044"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resolved bank account details",
                            content = @Content(
                                    schema = @Schema(implementation = ResolvedBankAccountResource.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "accountNumber": "01234567890",
                                                      "accountName": "John Doe"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid account number or bank code"
                    )
            }
    )
    @GetMapping("/resolve-account")
    public ResolvedBankAccountResource resolveAccountNumber(
            @Valid @RequestBody final ResolveBankAccountDto resolveBankAccountDto
    ) {
        return this.bankDataService.resolveAccountNumber(resolveBankAccountDto);
    }

    @Operation(
            summary = "Upload bank account",
            description = "Uploads a user's Nigerian bank account details for future transactions.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Bank account details to upload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UploadBankAccountDto.class),
                            examples = @ExampleObject(
                                    name = "Upload Bank Account Example",
                                    value = """
                                            {
                                              "account_number": "01234567890",
                                              "bank_code": "044"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Bank account successfully uploaded",
                            content = @Content(
                                    schema = @Schema(implementation = BankAccountResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user"
                    )
            }
    )
    @PostMapping("/upload-account")
    public BankAccountResource uploadBankAccount(
            final @NonNull Authentication authentication,
            @Valid @RequestBody final UploadBankAccountDto uploadBankAccountDto
    ) {
        return this.bankDataService.uploadBankAccount(authentication, uploadBankAccountDto);
    }

    @Operation(
            summary = "Update bank account",
            description = "Updates an existing user's Nigerian bank account details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated bank account details",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UploadBankAccountDto.class),
                            examples = @ExampleObject(
                                    name = "Update Bank Account Example",
                                    value = """
                                            {
                                              "account_number": "09876543210",
                                              "bank_code": "058"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Bank account successfully updated",
                            content = @Content(
                                    schema = @Schema(implementation = BankAccountResource.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Bank account not found"
                    )
            }
    )
    @PatchMapping("/update-account")
    public BankAccountResource updateBankAccount(
            final @NonNull Authentication authentication,
            @Valid @RequestBody final UploadBankAccountDto uploadBankAccountDto
    ) {
        return this.bankDataService.updateBankAccount(authentication, uploadBankAccountDto);
    }
}
