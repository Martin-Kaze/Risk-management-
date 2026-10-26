package com.martin.risk.engine;


import com.martin.risk.engine.rules.R1AmountSpikeRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class R1AmountSpikeRuleTest {

    R1AmountSpikeRule rule = new R1AmountSpikeRule();

    private ClientContext contextWithAvgAndCount (BigDecimal avg, int count) {
        return new ClientContext(avg, count, Set.of(), Set.of());
    }

    @Test
    void triggerSmallAvg(){

        Payment payment = new Payment("C-ID", new BigDecimal(10), "C-ID", "LT", LocalDateTime.now());
        assertTrue(rule.matches(payment, contextWithAvgAndCount(BigDecimal.ONE, 5)));
    }

    @Test
    void triggerNotBigAVG(){

        Payment payment = new Payment("C-ID", new BigDecimal(1000), "C-ID", "LT", LocalDateTime.now());
        assertFalse(rule.matches(payment, contextWithAvgAndCount(new BigDecimal(9999), 5)));
    }

    @Test
    void triggerNotLessThenFiveAvg(){

        Payment payment = new Payment("C-ID", new BigDecimal(999999), "C-ID", "LT", LocalDateTime.now());
        assertFalse(rule.matches(payment, contextWithAvgAndCount(new BigDecimal(1), 4)));
    }
}
