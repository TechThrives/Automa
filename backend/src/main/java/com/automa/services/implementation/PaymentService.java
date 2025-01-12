package com.automa.services.implementation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.automa.dto.payment.CheckoutRequest;
import com.automa.dto.payment.PaymentSession;
import com.automa.entity.ApplicationUser;
import com.automa.entity.payment.Payment;
import com.automa.entity.payment.PaymentStatus;
import com.automa.repository.ApplicationUserRepository;
import com.automa.repository.PaymentRepository;
import com.automa.services.interfaces.IPayment;
import com.automa.utils.ContextUtils;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;

@Service
@Validated
public class PaymentService implements IPayment {

    @Value("${frontend.uri}")
    private String frontendUri;

    private final PaymentRepository paymentRepository;
    private final ApplicationUserRepository applicationUserRepository;

    public PaymentService(PaymentRepository paymentRepository, ApplicationUserRepository applicationUserRepository) {
        this.paymentRepository = paymentRepository;
        this.applicationUserRepository = applicationUserRepository;
    }

    public Customer getOrCreateCustomer(ApplicationUser user) throws StripeException {
        CustomerListParams params = CustomerListParams.builder()
                .setEmail(user.getEmail())
                .setLimit(1L)
                .build();

        CustomerCollection customers = Customer.list(params);
        Customer customer;

        if (customers.getData().isEmpty()) {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setName(user.getFirstName() + " " + user.getLastName())
                    .setEmail(user.getEmail())
                    .setPhone(user.getPhoneNumber())
                    .setDescription("New customer created through API")
                    .build();
            customer = Customer.create(customerParams);
        } else {
            customer = customers.getData().get(0);
        }

        return customer;
    }

    @Override
    public PaymentSession createSession(CheckoutRequest checkoutRequest) throws StripeException {
        ApplicationUser user = applicationUserRepository.findByUsername(ContextUtils.getUsername())
        .orElseThrow(() -> new RuntimeException("User Not Found"));

        int creditsToPurchase = checkoutRequest.getCredits();

        String successURL = frontendUri + "/payment-success?session_id={CHECKOUT_SESSION_ID}";

        BigDecimal unitAmount = new BigDecimal(creditsToPurchase).multiply(new BigDecimal("100"));

        long unitAmountWithTaxInPaise = unitAmount.longValueExact();

        PriceData sessionItemPriceData = PriceData.builder()
        .setCurrency("inr")
        .setUnitAmount(unitAmountWithTaxInPaise)
        .setProductData(
                PriceData.ProductData.builder()
                        .setName("Purchase Credits")
                        .build())
        .build();

        LineItem sessionItem = LineItem.builder()
        .setPriceData(sessionItemPriceData)
        .setQuantity(1L)
        .build(); ;

        Customer customer = getOrCreateCustomer(user);

        Map<String, String> metadata = new HashMap<>();
        metadata.put("creditsToPurchase", String.valueOf(creditsToPurchase));
        metadata.put("grandTotal", String.valueOf(unitAmountWithTaxInPaise));

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCurrency("inr")
                .setCustomer(customer.getId())
                .addLineItem(sessionItem)
                .putAllMetadata(metadata)
                .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                .setReturnUrl(successURL)
                .build();

        Session session = Session.create(params);

        PaymentSession paymentSession = new PaymentSession();
        paymentSession.setSessionId(session.getId());

        return paymentSession;
    }

    @Override
    public void processCreditPurchase(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        Map<String, String> metadata = session.getMetadata();
        int creditsPurchased = Integer.parseInt(metadata.get("creditsToPurchase"));

        ApplicationUser user = applicationUserRepository.findByUsername(ContextUtils.getUsername())
        .orElseThrow(() -> new RuntimeException("User Not Found"));
        
        user.setCredits(user.getCredits() + creditsPurchased);
        applicationUserRepository.save(user);

        // Save the transaction details to the database
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setSessionId(sessionId);
        payment.setCreditsPurchased(creditsPurchased);
        payment.setGrandTotal(Integer.parseInt(metadata.get("grandTotal")));
        payment.setStatus(mapPaymentStatus(session));

        PaymentIntentRetrieveParams params = PaymentIntentRetrieveParams.builder().build();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent(), params, null);
        String paymentMethodId = paymentIntent.getPaymentMethod();
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);

        payment.setMethod(mapPaymentMethod(paymentMethod));

        paymentRepository.save(payment);
    }

    public PaymentStatus mapPaymentStatus(Session session) {
        switch (session.getPaymentStatus()) {
            case "paid":
                return PaymentStatus.SUCCESS;
            case "unpaid":
                return PaymentStatus.FAILED;
            case "no_payment_required":
                return PaymentStatus.PENDING;
            default:
                return PaymentStatus.FAILED;
        }
    }

    public com.automa.entity.payment.PaymentMethod mapPaymentMethod(PaymentMethod paymentMethod) {
        switch (paymentMethod.getType()) {
            case "card":
                return com.automa.entity.payment.PaymentMethod.CARD;
            case "google_pay":
                return com.automa.entity.payment.PaymentMethod.GOOGLE_PAY;
            case "amazon_pay":
                return com.automa.entity.payment.PaymentMethod.AMAZON_PAY;
            default:
                return com.automa.entity.payment.PaymentMethod.NONE;
        }
    }
}
