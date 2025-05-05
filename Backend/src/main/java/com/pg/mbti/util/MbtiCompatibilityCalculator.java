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

@Component
@RequiredArgsConstructor
@Slf4j
public class MbtiCompatibilityCalculator {
    private final ObjectMapper objectMapper;
    private List<List<Double>> compatibilityMatrix;

    @PostConstruct
    public void init() {
        try {
            compatibilityMatrix = objectMapper.readValue(
                    new ClassPathResource("data/mbtiCompatibilityMatrix.json").getInputStream(),
                    new TypeReference<>() {
                    }
            );
            log.info("MBTI compatibility matrix loaded successfully");
        } catch (IOException e) {
            log.error("Failed to load MBTI compatibility matrix", e);
            throw new RuntimeException("Failed to initialize MBTI compatibility matrix", e);
        }
    }

    public double calculateCompatibility(MBTIType mbtiType1, MBTIType mbtiType2) {
        return compatibilityMatrix.get(mbtiType1.ordinal()).get(mbtiType2.ordinal());
    }
}