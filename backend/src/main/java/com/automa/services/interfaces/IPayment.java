package com.automa.services.interfaces;

import java.util.List;

import com.automa.dto.MessageResponse;
import com.automa.dto.payment.CheckoutRequest;
import com.automa.dto.payment.PaymentResponse;
import com.automa.dto.payment.PaymentSession;
import com.stripe.exception.StripeException;

public interface IPayment {
    PaymentSession createSession(CheckoutRequest checkoutRequest) throws StripeException;

    MessageResponse processCreditPurchase(String sessionId) throws StripeException;

    List<PaymentResponse> findByUser();
}
