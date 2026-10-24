package com.martin.risk.engine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ClientContext(BigDecimal avg30d, BigDecimal count30d, Set<String> knownRecipients, List<String> countriesLast10Min) {
}
