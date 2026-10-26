package com.martin.risk.engine;
import com.martin.risk.engine.rules.R3MultiCountryRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class R3MultiCountryRuleTest {

    private ClientContext contextWithCountries(String... countries){
        return new ClientContext( BigDecimal.ZERO, 0, Set.of(), Set.of(countries));
    }

    private final R3MultiCountryRule rule = new R3MultiCountryRule();

    @Test
    void triggersWhenThirdCountryIsNew(){

        Payment payment = new Payment("C-ID", new BigDecimal(1001), "R-ID", "UG",
                LocalDateTime.of(2026,11,10,4,59));

        assertTrue(rule.matches(payment, contextWithCountries("LT","ENG", "IT")));
    }
    @Test
    void doesNotTriggerWhenCurrentCountryRepeats(){
        Payment payment = new Payment("C-ID", new BigDecimal(1001), "R-ID", "UG",
                LocalDateTime.of(2026,11,10,4,59));

        assertFalse(rule.matches(payment, contextWithCountries()));

    }
    @Test
    void triggersWhenHistoryAlreadyHasThree(){
        Payment payment = new Payment("C-ID", new BigDecimal(1000), "R-ID", "UG",
                LocalDateTime.of(2026,11,10,4,59));
        assertTrue(rule.matches(payment, contextWithCountries("ENG", "IT")));
    }
    @Test
    void doesNotTriggerWithoutHistory(){
        Payment payment = new Payment("C-ID", new BigDecimal(1000), "R-ID", "UG",
                LocalDateTime.of(2026,11,10,4,59));
        assertFalse(rule.matches(payment, contextWithCountries("UG", "IT")));
    }
}
