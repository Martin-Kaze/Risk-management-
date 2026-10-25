package com.martin.risk.engine;


import com.martin.risk.engine.rules.R2NewRecipientRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class R2NewRecipientRuleTest {

    private final R2NewRecipientRule rules = new R2NewRecipientRule();
    private ClientContext contextWithRecipients(String... recipients) {
        return new ClientContext(BigDecimal.ZERO, 0, Set.of(recipients), Set.of());
    }

    @Test
    void triggersSumOverLimitAndNewUser(){



        Payment p = new Payment("C_ID", new BigDecimal("1001"), "R_ID", "LT",
                LocalDateTime.of(1,1,1,1,1));

        assertTrue(rules.matches(p,contextWithRecipients()));
    }
    @Test
    void triggerNotSumHighUserKnown(){

        Payment p = new Payment("C_ID", new BigDecimal("1001"), "R_ID", "LT",
                LocalDateTime.of(1,1,1,1,1));

        assertFalse(rules.matches(p,contextWithRecipients("R_ID")));
    }
    @Test
    void triggerNotSumLowKnownRec(){

        Payment p = new Payment("C_ID", new BigDecimal("1"), "R_ID", "LT",
                LocalDateTime.of(1,1,1,1,1));

        assertFalse(rules.matches(p,contextWithRecipients("R_ID")));
    }
}
