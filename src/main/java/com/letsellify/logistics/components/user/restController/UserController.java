package com.letsellify.logistics.components.user.restController;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.user.core.userManagement.UserDataService;
import com.letsellify.logistics.components.user.core.userManagement.rest.dto.CreateUserDto;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "API's for users")
@Slf4j
public class UserController {
    private final UserDataService userDataService;

    @Operation(
      description = "Register/Creates a user",
      summary = "Validates user credentials and returns authorization tokens"
    )
    @PostMapping("/register")
    public UserResource createUser(@Valid @RequestBody final CreateUserDto createUserDto) {
        return this.userDataService.createUser(createUserDto);
    }


    @Operation(
      description = "Get authenticated user detail",
      summary = "Provides the details of the currently authenticated user"
    )
    @GetMapping("/me")
    public UserResource getMyself(final Authentication authentication, final HttpServletRequest request) {
        log.info("User Details requested for: {} ", authentication.getName() );
        log.info("Domain: {}", request.getServerName());
        return this.userDataService.getUser(authentication.getName());
    }


//    @Operation(
//      description = "Set Kyc document type",
//      summary = "Set's the Kyc document type for the user, so upload of the document could occur"
//    )
//    @PostMapping("/type/document-type")
//    public KycResource setKycDocumentType(final Authentication authentication, @RequestBody final @Valid KycDocumentTypeDto kycDocumentTypeDto) {
//        return this.userDataService.setKycDocumentType(authentication,kycDocumentTypeDto);
//    }
//
//    @Operation(
//      description = "Upload Kyc document"
//    )
//    @PostMapping("/type/document-upload")
//    public KycDocumentUploadResource uploadKycDocument(final Authentication authentication, final MultipartFile multipartFile) {
//        return this.userDataService.uploadKycDocument(authentication,multipartFile);
//    }
}
