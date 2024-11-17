package com.delivery_project.controller.api;

import com.delivery_project.dto.request.PaymentRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.PaymentResponseDto;
import com.delivery_project.entity.Payment;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.OrderService;
import com.delivery_project.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentRestController {

    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public ResponseEntity<?> createPayment(
            @PathVariable UUID orderId,
            @Valid @RequestBody PaymentRequestDto.Create paymentRequestDto) {
        paymentService.createPayment(orderId, paymentRequestDto);
        return ResponseEntity.ok(new MessageResponseDto("Payment" + SuccessMessage.CREATE.getMessage()));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable UUID paymentId) {
        PaymentResponseDto paymentResponseDto = paymentService.getPaymentResponse(paymentId);
        return ResponseEntity.ok().body(paymentResponseDto);
    }

    @PatchMapping("/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable UUID paymentId,
                                           @AuthenticationPrincipal UserDetailsImpl user) {
        paymentService.deletePayment(paymentId,user.getUser());
        return ResponseEntity.ok(new MessageResponseDto("payment" + SuccessMessage.DELETE.getMessage()));
    }
}
