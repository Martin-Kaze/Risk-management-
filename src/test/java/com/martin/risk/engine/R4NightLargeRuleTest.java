package com.martin.risk.engine;

import com.martin.risk.engine.rules.R4NightLargeRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class R4NightLargeRuleTest {

    private  final R4NightLargeRule rule = new R4NightLargeRule();
    private final  ClientContext ctx = new ClientContext(BigDecimal.ZERO, 0 , Set.of(), Set.of());

    @Test
    void triggersAtLastMinuteBeforeFive(){
        Payment p = new Payment( "C_ID", new BigDecimal("600"), "R_ID", "LT",
                LocalDateTime.of(2026,11,10,4,59));

        assertTrue(rule.matches(p,ctx));
    }
    @Test
    void doesTriggersAtLastMinuteBeforeFiveSmallAmount(){
        Payment p = new Payment( "C_ID", new BigDecimal("100"), "R_ID", "LT",
                LocalDateTime.of(2026,11,10,4,59));

        assertFalse(rule.matches(p,ctx));
    }
    @Test
    void doesNotTriggerPastFiveOClock(){
        Payment p = new Payment( "C_ID", new BigDecimal("600"), "R_ID", "LT",
                LocalDateTime.of(2026,11,10,5,1));

        assertFalse(rule.matches(p,ctx));
    }
    @Test
    void doesNotTriggerPastFiveOClockBigAmount(){
        Payment p = new Payment( "C_ID", new BigDecimal("99999"), "R_ID", "LT",
                LocalDateTime.of(2026,11,10,5,1));

        assertFalse(rule.matches(p,ctx));
    }
}
