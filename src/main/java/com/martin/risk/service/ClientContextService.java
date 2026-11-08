package com.martin.risk.service;

import com.martin.risk.data.PaymentRecord;
import com.martin.risk.data.PaymentRecordRepository;
import com.martin.risk.engine.ClientContext;
import com.martin.risk.engine.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientContextService {
    private final PaymentRecordRepository paymentRecords;

    public ClientContextService(PaymentRecordRepository paymentRecords) {
        this.paymentRecords = paymentRecords;
    }

    public ClientContext build(String organizationId, Payment payment) {

        LocalDateTime now = payment.timestamp();

        List<PaymentRecord> last90 = paymentRecords.findByOrganizationIdAndClientIdAndTimestampBetween(
                organizationId, payment.clientId(), now.minusDays(90), now);

        List<PaymentRecord> last30 = last90.stream()
                .filter(record -> record.getTimestamp().isAfter(now.minusDays(30)))
                .toList();

        int count = last30.size();

        BigDecimal sum = last30.stream()
                .map(PaymentRecord::getAmountEur)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg = count == 0
                ? BigDecimal.ZERO
                : sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

        Set<String> knownRecipients = last90.stream()
                .map(PaymentRecord::getRecipientId)
                .collect(Collectors.toSet());

        Set<String> recentCountries = last90.stream()
                .filter(record -> !record.getTimestamp().isBefore(now.minusMinutes(10)))
                .map(PaymentRecord::getCountry)
                .collect(Collectors.toSet());

        return new ClientContext(avg, count, knownRecipients, recentCountries);
    }
}
