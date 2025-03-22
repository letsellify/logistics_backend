package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA;

import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource.StateLGAResource;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.rest.resource.StatesResource;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:44
 */

@Service
@RequiredArgsConstructor
public class StateLGADataService {
    private final StateLGAManager stateLGAManager;

    public StatesResource getStates() {
        return this.stateLGAManager.getAllStates()
                                   .getResource();
    }

    public StateLGAResource getStateLGA(final @NonNull String stateName) {
        try {
            return this.stateLGAManager.getStateLGA(stateName)
                                       .getResource();
        }
        catch (final NoSuchStateException e) {
            throw new LogisticsResourceNotFoundException(e);
        }
    }
}
