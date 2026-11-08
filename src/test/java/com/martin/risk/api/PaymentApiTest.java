package com.martin.risk.api;

import com.jayway.jsonpath.JsonPath;
import com.martin.risk.data.PaymentRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PaymentApiTest {

    private static final String PAYMENT = """
            {"clientId":"C2","amount":1000,"currency":"EUR","recipientId":"R-77",
             "country":"LT","timestamp":"2026-11-10T03:00:00"}""";

    @Autowired
    MockMvc mvc;

    @Autowired
    PaymentRecordRepository payments;

    @Test
    void invalidPaymentIsRejectedAndNotSaved() throws Exception {
        long before = payments.count();
        String badPayment = PAYMENT.replace("\"EUR\"", "\"XXX\"");

        mvc.perform(post("/payments").header("X-API-Key", "demo-key-a")
                        .contentType(MediaType.APPLICATION_JSON).content(badPayment))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("INVALID_PAYMENT"));

        assertEquals(before, payments.count());
    }

    @Test
    void wrongApiKeyIsRejected() throws Exception {
        mvc.perform(post("/payments").header("X-API-Key", "wrong-key")
                        .contentType(MediaType.APPLICATION_JSON).content(PAYMENT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void orgACannotReadOrgBPayment() throws Exception {
        String response = mvc.perform(post("/payments").header("X-API-Key", "demo-key-b")
                        .contentType(MediaType.APPLICATION_JSON).content(PAYMENT))
                .andExpect(jsonPath("$.decision").value("BLOCK"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(response, "$.paymentId");

        mvc.perform(get("/payments/" + id).header("X-API-Key", "demo-key-b"))
                .andExpect(status().isOk());
        mvc.perform(get("/payments/" + id).header("X-API-Key", "demo-key-a"))
                .andExpect(status().isNotFound());
    }
}
