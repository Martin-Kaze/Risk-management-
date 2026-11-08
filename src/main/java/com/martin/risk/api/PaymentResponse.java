package com.martin.risk.api;

import com.martin.risk.engine.Decision;
import com.martin.risk.engine.RuleResult;

import java.util.List;

public record PaymentResponse(Long paymentId, Decision decision, int score, List<RuleResult> triggeredRules) {
}
