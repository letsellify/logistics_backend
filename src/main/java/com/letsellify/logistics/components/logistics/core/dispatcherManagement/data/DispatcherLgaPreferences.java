package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper.DispatcherMapper;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.LgaPreferenceEntity;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherLgaPreferenceResources;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author: Ahmad Buba
 * Date:8/22/25
 */


public record DispatcherLgaPreferences(
        List<DispatcherLgaPreference> preferences
) {
    public DispatcherLgaPreferences(Set<LgaPreferenceEntity> entityList) {
        this(entityList.stream()
                .map(DispatcherLgaPreference::new)
                .toList()
        );
    }

    public DispatcherLgaPreferenceResources getResources() {
        return DispatcherMapper.INSTANCE.dataToResources(this);
    }

}

