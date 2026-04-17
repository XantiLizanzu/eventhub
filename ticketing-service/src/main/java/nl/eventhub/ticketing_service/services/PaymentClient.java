package nl.eventhub.ticketing_service.services;

import nl.eventhub.ticketing_service.models.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClient {


    private final RestTemplate restTemplate;

    @Value("${payments.service.url}")
    private String paymentServiceUrl;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void pay(Long ticketId) {
        String url = paymentServiceUrl + "payments/tickets/" + ticketId + "/init-payment";
        restTemplate.postForEntity(url, null, Payment.class);
    }
}
