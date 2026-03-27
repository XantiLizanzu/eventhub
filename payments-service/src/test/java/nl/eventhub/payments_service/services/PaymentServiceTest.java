package nl.eventhub.payments_service.services;

import nl.eventhub.payments_service.models.Payment;
import nl.eventhub.payments_service.models.PaymentStatus;
import nl.eventhub.payments_service.repositories.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment testPayment;
    private final Long ticketId = 1L;

    @BeforeEach
    void setUp() {
        testPayment = new Payment(ticketId, PaymentStatus.INITIATED, 1000);
    }

    @Test
    void initPayment_shouldCreatePaymentWithInitiatedStatus() {
        // Arrange
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        // Act
        Payment result = paymentService.initPayment(ticketId);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.INITIATED, result.getStatus());
        assertEquals(ticketId, result.getTicketId());
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
    }
}
