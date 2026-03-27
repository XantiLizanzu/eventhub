package nl.eventhub.payments_service.controllers;

import nl.eventhub.payments_service.models.Payment;
import nl.eventhub.payments_service.models.PaymentStatus;
import nl.eventhub.payments_service.services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment testPayment;
    private final Long ticketId = 1L;

    @BeforeEach
    void setUp() {
        testPayment = new Payment(ticketId, PaymentStatus.INITIATED, 1000);
    }

    @Test
    void initPayment_shouldReturnInitiatedPayment() {
        // Arrange
        when(paymentService.initPayment(anyLong())).thenReturn(testPayment);

        // Act
        ResponseEntity<Payment> result = paymentController.initPayment(ticketId);

        // Assert
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(testPayment, result.getBody());
        verify(paymentService, times(1)).initPayment(ticketId);
    }
}
