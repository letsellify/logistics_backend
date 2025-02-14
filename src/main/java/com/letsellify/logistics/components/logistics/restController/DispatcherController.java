package com.letsellify.logistics.components.logistics.restController;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;
import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:03:45
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dispatcher-management")
@Tag(name = "Dispatcher Management API", description = "API's for managing dispatchers")
public class DispatcherController {
    private final DispatcherDataService dispatcherDataService;

    @PostMapping("/kyc-upload")
    public String uploadKycDocument(
      final Authentication authentication,
      @RequestParam final KycDocumentType documentType,
      @RequestParam("file") final MultipartFile file
    ) {
        return this.dispatcherDataService.uploadKyc(authentication, documentType, file);
    }

//    @PostMapping(consumes = "multipart/form-data")
//    public ResponseEntity<String> uploadKyc(
//      @RequestParam("documentType") KycDocument kycDocument,
//      @RequestParam("file") MultipartFile file) {
//
//        // Validate that a file has been provided
//        if (file.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File must be provided.");
//        }
//
//        // Business logic to handle the KYC upload
//        saveKycDocument(documentType, file);
//
//        return ResponseEntity.ok("KYC document uploaded successfully!");
//    }
}
