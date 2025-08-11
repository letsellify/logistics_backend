package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement;

import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.rest.resource.StatesResource;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:44
 */

@Service
@RequiredArgsConstructor
public class NigeriaStatesDataService {
    private final NigeriaStatesManager nigeriaStatesManager;

    public StatesResource getStates() {
        return this.nigeriaStatesManager.getAllStates()
                .getResource();
    }

    public StateLGAResource getStateLGA(final @NonNull String stateName) {
        try {
            return this.nigeriaStatesManager.getStateLGA(stateName)
                    .getResource();
        } catch (final NoSuchStateException e) {
            throw new LogisticsResourceNotFoundException(e);
        }
    }
}
