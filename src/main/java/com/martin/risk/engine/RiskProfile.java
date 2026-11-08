package com.martin.risk.engine;

import java.util.Map;

public record RiskProfile(Map<RuleNr, Integer> points, int reviewThreshold, int blockThreshold) {

    public static final Map<RuleNr, Integer> DEFAULT_POINTS = Map.of(
            RuleNr.R1_AMOUNT_SPIKE, 30,
            RuleNr.R2_NEW_RECIPIENT, 25,
            RuleNr.R3_MULTI_COUNTRY, 40,
            RuleNr.R4_NIGHT_LARGE, 15
    );

    public RiskProfile {
        if (reviewThreshold >= blockThreshold) {
            throw new IllegalArgumentException("REVIEW threshold must be lower than BLOCK threshold");
        }
    }

    public static RiskProfile defaults() {
        return new RiskProfile(DEFAULT_POINTS, 40, 70);
    }
}
