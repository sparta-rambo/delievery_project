package com.delivery_project.service;

import com.delivery_project.dto.request.PaymentRequestDto;
import com.delivery_project.dto.response.PaymentResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.Payment;
import com.delivery_project.entity.QPayment;
import com.delivery_project.entity.User;
import com.delivery_project.repository.jpa.OrderRepository;
import com.delivery_project.repository.jpa.PaymentRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public Payment getPaymentById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new NullPointerException("해당 결제 내용이 존재하지 않습니다."));
    }

    public void createPayment(UUID orderId, PaymentRequestDto.Create paymentRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NullPointerException("존재하지 않는 주문 항목입니다."));

        try {
            Payment payment = Payment.builder()
                    .paymentRequestDto(paymentRequestDto)
                    .order(order)
                    .build();
            order.checkedPayment();
            orderRepository.save(order);
            paymentRepository.save(payment);
        } catch (Exception e) {
            order.canceledPayment();
            orderRepository.save(order);
        }
    }

    public PaymentResponseDto getPaymentResponse(UUID paymentId) {
        return paymentRepository.findPaymentResponseDtoByPaymentId(paymentId).orElseThrow(() -> new NullPointerException("결제 내역이 존재하지 않습니다."));
    }

    public void deletePayment(UUID id, User user) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new NullPointerException("결제 내역이 없습니다."));
        payment.delete(user.getUsername());
        paymentRepository.save(payment);
    }

    public BooleanExpression createPredicate(Integer amount, String paymentMethod) {
        QPayment qPayment = QPayment.payment;
        BooleanExpression predicate = Expressions.asBoolean(true).isTrue();

        if (amount != null) {
            predicate = predicate.and(qPayment.amount.eq(amount));
        }
        if (paymentMethod != null) {
            predicate = predicate.and(qPayment.paymentMethod.eq(paymentMethod));
        }
        return predicate;
    }

    public Page<PaymentResponseDto> getAllPayments(PageRequest pageRequest, Integer amount, String paymentMethod) {
        BooleanExpression predicate = createPredicate(amount, paymentMethod);
        return paymentRepository.findAllPayments(predicate, pageRequest);
    }

}
