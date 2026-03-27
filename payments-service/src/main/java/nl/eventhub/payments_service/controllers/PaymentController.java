package nl.eventhub.payments_service.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.eventhub.payments_service.models.Payment;
import nl.eventhub.payments_service.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Payments")
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/tickets/{ticketId}/init-payment")
    @Operation(summary = "Initialize payment")
    public ResponseEntity<Payment> initPayment(@PathVariable Long ticketId) {
        return ResponseEntity.ok(paymentService.initPayment(ticketId));
    }
}
