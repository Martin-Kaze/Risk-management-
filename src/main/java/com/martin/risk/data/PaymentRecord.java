package com.martin.risk.data;

import com.martin.risk.engine.Decision;
import com.martin.risk.engine.Payment;
import com.martin.risk.engine.RiskResult;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class PaymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String organizationId;
    private String clientId;
    private String recipientId;
    private String country;
    private BigDecimal amountEur;
    private LocalDateTime timestamp;
    private int score;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    protected PaymentRecord() {
    }

    public PaymentRecord(String organizationId, Payment payment, RiskResult result) {
        this.organizationId = organizationId;
        this.clientId = payment.clientId();
        this.recipientId = payment.recipientId();
        this.country = payment.countryOrigin();
        this.amountEur = payment.amountEur();
        this.timestamp = payment.timestamp();
        this.score = result.score();
        this.decision = result.decision();
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getCountry() {
        return country;
    }

    public BigDecimal getAmountEur() {
        return amountEur;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getScore() {
        return score;
    }

    public Decision getDecision() {
        return decision;
    }
}
