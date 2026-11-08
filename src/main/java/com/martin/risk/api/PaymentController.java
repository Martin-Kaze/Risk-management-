package com.martin.risk.api;

import com.martin.risk.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public PaymentResponse evaluate(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @Valid @RequestBody PaymentRequest request){
        return service.evaluate(apiKey, request);
    }

    @GetMapping("/{id}")
    public PaymentView get(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @PathVariable Long id) {
        return service.get(apiKey, id);
    }

}
