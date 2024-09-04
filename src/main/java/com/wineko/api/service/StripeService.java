package com.wineko.api.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    public StripeService() {
        Stripe.apiKey = "sk_test_51NBHfWHRXxl48zKyV5Qr5pYvGUxQ8pRqbxFqqb9kwivS5x5CZzyPFXr7NF6YSEH0vJYUKyZtBiisi17dHEASWfgC00OAY5cP4H";
    }

    public String createCheckoutSession(List<Map<String, Object>> items) {
        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("http://localhost:4200/success")
                    .setCancelUrl("http://localhost:4200/cancel");

            for (Map<String, Object> item : items) {
                paramsBuilder.addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(((Number) item.get("quantity")).longValue())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(((Number) item.get("price")).longValue() * 100)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName((String) item.get("title"))
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
            }

            SessionCreateParams params = paramsBuilder.build();
            Session session = Session.create(params);
            return session.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Stripe session", e);
        }
    }
}
