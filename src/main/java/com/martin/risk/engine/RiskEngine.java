package com.martin.risk.engine;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RiskEngine {

    private final List<RiskRule> rules;
    private static final int MAX_SCORE = 100;

    public RiskEngine(List<RiskRule> rules) {
        this.rules = rules;
    }

    public RiskResult evaluate ( Payment payment , ClientContext ctx, RiskProfile profile) {
        List<RuleResult> triggered = new ArrayList<>();
        int score = 0;

        for ( RiskRule rule : rules){

            int points = profile.points().getOrDefault(rule.code(), 0);

            if( points >  0 &&  rule.matches(payment, ctx)){
                triggered.add(new RuleResult( rule.code(), points));
                score += points;
            }

        }
        score = Math.min(MAX_SCORE, score);
        return new RiskResult(decide( score, profile), score,triggered);
    }

    private Decision decide( int score, RiskProfile profile){

        if (score >= profile.reviewThreshold()){

            if(score >= profile.blockThreshold()){
                return Decision.BLOCK;
            }
            return  Decision.REVIEW;
        }
        return  Decision.APPROVE;
    }

}
