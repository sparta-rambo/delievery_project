package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.PaymentResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryCustom {

    Optional<PaymentResponseDto> findPaymentResponseDtoByPaymentId(UUID paymentId);
}
