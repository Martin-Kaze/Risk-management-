package com.martin.risk.engine.rules;

import com.martin.risk.engine.Decision;
import com.martin.risk.engine.RuleResult;

import java.util.List;

public record RiskResult(Decision decision, int score, List<RuleResult> triggeredRules) { }
