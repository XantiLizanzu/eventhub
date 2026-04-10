package nl.eventhub.ticketing_service.models;

public class Payment {
    private Long id;
    private Long ticketId;
    private PaymentStatus status;
    private Integer amountInCents;
}
