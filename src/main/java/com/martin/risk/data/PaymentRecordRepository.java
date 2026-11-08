package com.martin.risk.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    List<PaymentRecord> findByOrganizationIdAndClientIdAndTimestampBetween(
            String organizationId, String clientId, LocalDateTime from, LocalDateTime to);

    Optional<PaymentRecord> findByIdAndOrganizationId(Long id, String organizationId);

}
