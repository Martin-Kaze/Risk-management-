package com.martin.risk.engine;

import java.math.BigDecimal;
import java.util.Set;

public record ClientContext(BigDecimal avg30d, int count30d, Set<String> knownRecipients90d, Set<String> countriesLast10Min) {
}
