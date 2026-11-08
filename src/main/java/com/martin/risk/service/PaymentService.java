package com.martin.risk.service;

import com.martin.risk.api.PaymentRequest;
import com.martin.risk.api.PaymentResponse;
import com.martin.risk.data.Organization;
import com.martin.risk.data.OrganizationRepository;
import com.martin.risk.data.PaymentRecord;
import com.martin.risk.data.PaymentRecordRepository;
import com.martin.risk.engine.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final OrganizationRepository organizations;
    private final PaymentRecordRepository payments;
    private final ClientContextService contextService;
    private final CurrencyConverter converter;
    private final RiskEngine riskEngine;

    public PaymentService(OrganizationRepository organization, PaymentRecordRepository payments, ClientContextService contextService, CurrencyConverter converter, RiskEngine riskEngine) {
        this.organizations = organization;
        this.payments = payments;
        this.contextService = contextService;
        this.converter = converter;
        this.riskEngine = riskEngine;
    }

    public PaymentResponse evaluate (String apiKey, PaymentRequest request){
        Organization org = authenticate(apiKey);
        Payment payment = new Payment(request.clientId(), converter.toEur(request.amount(), request.currency()),
                                request.recipientId(), request.country(), request.timestamp());

        ClientContext ctx = contextService.build(org.getId(), payment);

        RiskProfile profile = new RiskProfile(RiskProfile.DEFAULT_POINTS, org.getReviewThreshold(), org.getBlockThreshold());
        RiskResult result =  riskEngine.evaluate(payment, ctx, profile);

        PaymentRecord saved = payments.save(new PaymentRecord(org.getId(), payment, result));

        return new PaymentResponse(saved.getId(), result.decision(), result.score(), result.triggeredRules());

    }


    private Organization authenticate( String apiKey){
        if (apiKey == null){
            throw new UnauthorizedException();
        }
        return organizations.findByApiKey(apiKey).orElseThrow(UnauthorizedException::new);
    }



}
