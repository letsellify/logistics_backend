package com.letsellify.logistics.components.logistics.restController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherProfileInfoResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:03:45
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispatcher/")
@Tag(name = "Dispatchers API", description = "API's for dispatchers")
public class DispatcherController {
    private final DispatcherDataService dispatcherDataService;

//    @PostMapping("type-upload")
//    public String uploadKycDocument(final @NonNull Authentication authentication, @RequestParam final @NonNull KycDocumentType documentType, @RequestParam final MultipartFile file, HttpServletRequest httpServletRequest) {
//        if (file.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty.");
//        }
//        final String contentType = file.getContentType();
//        if (contentType == null || !List.of("image/jpeg", "image/png", "application/pdf").contains(contentType)) {
//            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only JPEG, PNG, and PDF files are allowed.");
//        }
//        System.out.println("File received: " + file.getOriginalFilename() + " with content type: " + contentType);
//        Cookie[] cookies = httpServletRequest.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("access_token".equals(cookie.getName())) { // replace with your actual cookie name
//                    String token = cookie.getValue();
//                    System.out.println("Access Token from Cookie: " + token);
//                }
//            }
//        } else {
//            System.out.println("No cookies found in request.");
//        }
//
//        return this.dispatcherDataService.uploadKyc(authentication, documentType, file);
//    }

//    @DeleteMapping("type-upload")
//    public void deleteKyc(final @NonNull Authentication authentication, @RequestParam final @NonNull String kycIdentification) {
//        this.dispatcherDataService.deleteKyc(authentication, kycIdentification);
//    }

    @PostMapping("profile")
    public DispatcherProfileInfoResource setProfile(final @NonNull Authentication authentication, @RequestBody final  @Valid DispatcherProfileDto infoDto) {
        return this.dispatcherDataService.setProfile(authentication, infoDto);
    }

    @GetMapping("profile")
    public DispatcherProfileInfoResource getProfile(final @NonNull Authentication authentication) {
        return this.dispatcherDataService.getProfile(authentication);
    }


//    @PostMapping("confirm-info")
//    public LogisticDispatcherInfoResource confirmInfoSubmissionForApproval(final @NonNull Authentication authentication) {
//        return this.dispatcherDataService.confirmInfoSubmissionForApproval(authentication);
//    }
//
//    @GetMapping("view-info")
//    public LogisticDispatcherInfoResource viewPersonalInfoAfterApproval(final @NonNull Authentication authentication) {
//        return this.dispatcherDataService.viewPersonalInfoAfterApproval(authentication);
//    }

//    @PatchMapping("delivery-status")
//    public DispatcherResource setCurrentlyAcceptingDelivery(final @NonNull Authentication authentication, @RequestParam(name = "accepting") final boolean acceptingDelivery) {
//        return this.dispatcherDataService.setCurrentlyAcceptingDelivery(authentication, acceptingDelivery);
//    }

}
