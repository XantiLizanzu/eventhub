package nl.eventhub.payments_service.services;
import nl.eventhub.payments_service.models.Payment;
import nl.eventhub.payments_service.models.PaymentStatus;
import nl.eventhub.payments_service.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment initPayment(Long ticketId) {
        Payment payment = new Payment(ticketId, PaymentStatus.INITIATED, 1000); // Dummy amount 1000 cents
        payment = paymentRepository.save(payment);

        final Long paymentId = payment.getId();
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000); // wait 5 seconds
                paymentRepository.findById(paymentId).ifPresent(p -> {
                    p.setStatus(PaymentStatus.SUCCESSFUL);
                    paymentRepository.save(p);
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return payment;
    }
}
