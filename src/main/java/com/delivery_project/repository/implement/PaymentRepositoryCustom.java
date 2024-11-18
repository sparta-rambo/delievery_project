package com.delivery_project.repository.implement;

import com.delivery_project.dto.response.PaymentResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepositoryCustom {

    Optional<PaymentResponseDto> findPaymentResponseDtoByPaymentId(UUID paymentId);

    Page<PaymentResponseDto> findAllPayments(BooleanExpression predicate, PageRequest pageRequest);
}
