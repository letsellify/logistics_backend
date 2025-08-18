package com.letsellify.logistics.components.logistics.core.guarantorManagement;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Ahmad Buba
 * Date: 8/18/25
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class GuarantorDataService {
    private final GuarantorManager guarantorManager;

    public List<String> getAllGuarantorCareers() {
        return this.guarantorManager.getAllCareers();
    }

    public List<String> getAllGuarantorRelationships() {
        return this.guarantorManager.getAllRelationships();
    }
}
