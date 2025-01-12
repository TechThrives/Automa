package com.automa.services.interfaces;

import com.automa.dto.payment.CheckoutRequest;
import com.automa.dto.payment.PaymentSession;
import com.stripe.exception.StripeException;

public interface IPayment {
    PaymentSession createSession(CheckoutRequest checkoutRequest) throws StripeException;

    void processCreditPurchase(String sessionId) throws StripeException;
}
