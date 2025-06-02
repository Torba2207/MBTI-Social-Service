package com.pg.mbti.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pg.mbti.enums.MBTIType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Component responsible for loading and providing MBTI compatibility scores from a JSON matrix.
 * The compatibility matrix is loaded during application startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MbtiCompatibilityCalculator {
    private final ObjectMapper objectMapper;
    private List<List<Double>> compatibilityMatrix; // Stores the loaded compatibility matrix

    /**
     * Initializes the compatibility matrix by loading it from a JSON file.
     * This method is automatically called after the bean's properties are set.
     *
     * @throws RuntimeException If the compatibility matrix fails to load from the JSON file.
     */
    @PostConstruct
    public void init() {
        try {
            log.info("Attempting to load MBTI compatibility matrix from 'data/mbtiCompatibilityMatrix.json'"); // Log loading attempt
            compatibilityMatrix = objectMapper.readValue(
                    new ClassPathResource("data/mbtiCompatibilityMatrix.json").getInputStream(),
                    new TypeReference<>() {
                    }
            );
            log.info("MBTI compatibility matrix loaded successfully"); // Log successful loading
        } catch (IOException e) {
            log.error("Failed to load MBTI compatibility matrix", e); // Log failure to load matrix
            throw new RuntimeException("Failed to initialize MBTI compatibility matrix", e);
        }
    }

    /**
     * Calculates the compatibility score between two MBTI types using the loaded matrix.
     * The compatibility is symmetrical, meaning A-B is the same as B-A.
     *
     * @param mbtiType1 The first {@link MBTIType}.
     * @param mbtiType2 The second {@link MBTIType}.
     * @return A double representing the compatibility score.
     */
    public double calculateCompatibility(MBTIType mbtiType1, MBTIType mbtiType2) {
        // MBTIType.ordinal() returns the enum's declaration order (0-indexed),
        // which corresponds to the indices in the compatibility matrix.
        log.debug("Calculating compatibility between {} (ordinal: {}) and {} (ordinal: {})",
                mbtiType1, mbtiType1.ordinal(), mbtiType2, mbtiType2.ordinal()); // Log compatibility calculation
        return compatibilityMatrix.get(mbtiType1.ordinal()).get(mbtiType2.ordinal());
    }
}