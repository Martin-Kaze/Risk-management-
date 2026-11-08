package com.martin.risk.engine;

import java.util.List;

public record RiskResult(Decision decision, int score, List<RuleResult> triggeredRules) { }
