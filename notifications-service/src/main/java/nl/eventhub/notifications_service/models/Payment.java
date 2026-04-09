package nl.eventhub.notifications_service.models;

public class Payment {
    private Long id;

    private Long ticketId;
    private PaymentStatus status;
    private Integer amountInCents;

    public Payment() {}

    public Payment(Long ticketId, PaymentStatus status, Integer amountInCents) {
        this.ticketId = ticketId;
        this.status = status;
        this.amountInCents = amountInCents;
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Integer getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(Integer amountInCents) {
        this.amountInCents = amountInCents;
    }

}
