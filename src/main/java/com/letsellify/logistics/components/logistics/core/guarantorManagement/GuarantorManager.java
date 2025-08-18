package com.letsellify.logistics.components.logistics.core.guarantorManagement;


import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Author: Ahmad Buba
 * Date: 8/18/25
 */

@Component
@Slf4j
public class GuarantorManager {

    List<String> getAllRelationships() {
        return Arrays.stream(GuarantorRelationship.values())
                .map(Enum::name)
                .toList();
    }

    List<String> getAllCareers() {
        return Arrays.stream(GuarantorCareer.values())
                .map(Enum::name)
                .toList();
    }
}
