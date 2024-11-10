package com.delivery_project.controller.api;

import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto.Create orderRequestDto) {
        //security
        User user = new User();

        orderService.createOrder(orderRequestDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Order" + SuccessMessage.CREATE.getMessage()));
    }
}
