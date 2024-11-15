package com.delivery_project.controller.api;

import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.OrderResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDto.Create orderRequestDto) {
        //security
        User user = new User();

        orderService.createOrder(orderRequestDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Order" + SuccessMessage.CREATE.getMessage()));
    }

    @GetMapping("/{orderId}")
//    @PreAuthorize("@orderPermissionService.canAccessOrder(#order, #userDetails)")
    public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
        OrderResponseDto orderResponseDto = orderService.findOrderDetails(orderId);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID order_id) {
        return ResponseEntity.ok().body(new MessageResponseDto(""));
    }
}
