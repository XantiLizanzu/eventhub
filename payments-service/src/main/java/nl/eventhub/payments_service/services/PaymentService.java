package nl.eventhub.payments_service.services;
import nl.eventhub.payments_service.models.Payment;
import nl.eventhub.payments_service.models.PaymentStatus;
import nl.eventhub.payments_service.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private TicketingClient ticketingClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMessageSender paymentMessageSender;

    public Payment initPayment(Long ticketId) {
        Payment payment = new Payment(ticketId, PaymentStatus.SUCCESSFUL, 1000); // Dummy amount 1000 cents
        Payment newPayment = paymentRepository.save(payment);
        ticketingClient.completeTicket(newPayment.getTicketId());
        paymentMessageSender.sendPaymentSucceededQueueName(newPayment);
        return newPayment;
    }
}
