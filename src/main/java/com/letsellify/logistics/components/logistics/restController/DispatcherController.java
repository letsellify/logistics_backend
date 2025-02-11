package com.letsellify.logistics.components.logistics.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.DispatcherDataService;

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
