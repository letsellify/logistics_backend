package com.letsellify.logistics.components.logistic.core.nigeriaStateLGA;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.config.StateLGAProps;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.data.NigerianStateLGA;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.data.NigerianStates;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.database.entity.LGAEntity;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.database.entity.StateEntity;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.database.repository.LGARepository;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.database.repository.StateRepository;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistic.core.nigeriaStateLGA.exception.StateLGAFileNotFountException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:33
 */

@Component
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(StateLGAProps.class)
public class StateLGAManager implements CommandLineRunner {
    private final StateRepository stateRepository;
    private final LGARepository lgaRepository;
    private final StateLGAProps stateLGAProps;
    private final Map<String, Set<String>> stateLgaCache = new HashMap<>();


    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        if (this.stateRepository.count() <= 0) {
            log.info("Actually loading from file source since repository is empty");
            final String jsonFilePath = this.stateLGAProps.jsonFilePath();
            final InputStream jsonInputStream;

            if (jsonFilePath.startsWith("classpath:")) {
                // Load from classpath
                jsonInputStream = this.getClass().getClassLoader().getResourceAsStream(jsonFilePath.replace("classpath:", ""));
            } else {
                // Load from file system
                jsonInputStream = new FileInputStream(jsonFilePath);
            }

            if (jsonInputStream != null) {
                this.populateDataFromJson(jsonInputStream);
            } else {
                throw new StateLGAFileNotFountException("JSON file not found: " + jsonFilePath);
            }
        }
        this.preloadCache();
    }

    public boolean validateStateAndLgaForLogistics(final @NonNull String currentState, final @NonNull String currentLga, final @NonNull String shippingState, final @NonNull String shippingLga) throws NoSuchStateException {
//        // Validate current homeState and LGA using cache
//        this.validateStateAndLgaFromCache(currentState, currentLga);
//
//        // Validate shipping homeState and LGA using cache
//        this.validateStateAndLgaFromCache(shippingState, shippingLga);
        return this.validateStateLga(currentState, currentLga) && this.validateStateLga(shippingState, shippingLga);
    }

    public boolean validateStateLga(final @NonNull String state, final @NonNull String lga) throws NoSuchStateException {
        return this.validateStateAndLgaFromCache(state,lga);
    }


    private boolean validateStateAndLgaFromCache(final @NonNull String state, final @NonNull String lga) throws NoSuchStateException {
        // Check if the homeState exists in cache (case-insensitive)
        final Set<String> lgas = this.stateLgaCache.get(state);
        log.info("State requested, {}", state);
        log.info("lGAs of homeState requested, {}", lgas.toString());
        if (lgas.isEmpty()) {
            throw new NoSuchStateException("No such homeState exists: " + state);
        }
        // Check if the LGA belongs to the homeState
        return lgas.contains(lga);
    }

    public NigerianStates getAllStates() {
        return new NigerianStates(new ArrayList<>(this.stateLgaCache.keySet()));
    }

    public boolean stateExists(final String stateName) {
        return this.stateLgaCache.containsKey(stateName.toLowerCase());
    }

    public NigerianStateLGA getStateLGA(final @NonNull String stateName) throws NoSuchStateException {
        final Set<String> lgas = this.stateLgaCache.get(stateName);
        if (lgas == null) {
            throw new NoSuchStateException("No such homeState exists: " + stateName);
        }
        return new NigerianStateLGA(stateName, lgas);
    }

    private void populateDataFromJson(final InputStream jsonInputStream) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<Map<String, List<String>>> typeReference = new TypeReference<>() {};
        final Map<String, List<String>> statesAndLgas = objectMapper.readValue(jsonInputStream, typeReference);

        for (final Map.Entry<String, List<String>> entry : statesAndLgas.entrySet()) {
            final String stateName = entry.getKey();
            final List<String> lgas = entry.getValue();

            // Create and save State
            StateEntity state = new StateEntity();
            state.setName(stateName);
            state = this.stateRepository.save(state);

            // Create and save LGAs
            for (final String lgaName : lgas) {
                final LGAEntity lga = new LGAEntity();
                lga.setName(lgaName.trim());
                lga.setState(state);
                this.lgaRepository.save(lga);
            }
        }
    }

    private void preloadCache() {
        log.info("Preloading from cache");
        this.stateRepository.findAll().forEach(state -> {
            log.info("the current homeState: {}", state.getName());
            final Set<String> lgaSet = this.lgaRepository.findByState(state).stream()
                                                         .map(lga -> {
                                                             log.info("the homeLga: {}", lga.getName());
                                                             return lga.getName();
                                                         })
                                                         .collect(Collectors.toSet());
            this.stateLgaCache.put(state.getName(), lgaSet);
        });
    }
}
