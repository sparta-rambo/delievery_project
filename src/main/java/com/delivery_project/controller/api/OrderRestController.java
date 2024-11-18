package com.delivery_project.controller.api;

import com.delivery_project.common.utils.PageRequestUtils;
import com.delivery_project.dto.request.OrderRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.dto.response.OrderResponseDto;
import com.delivery_project.entity.Order;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderRestController {

    private final OrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequestDto.Create orderRequestDto,
                                         @AuthenticationPrincipal User user) {
        orderService.createOrder(orderRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDto("Order" + SuccessMessage.CREATE.getMessage()));
    }

    @GetMapping("/{orderId}")
//    @PreAuthorize("@permissionHandler.canAccessOrder(#order, #userDetails)")
    public ResponseEntity<?> getOrderById(@PathVariable UUID orderId) {
        OrderResponseDto orderResponseDto = orderService.findOrderDetails(orderId);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @GetMapping()
    public ResponseEntity<Page<OrderResponseDto>> getAllOrderDetails(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String restaurantName,
            @RequestParam(required = false) String orderType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortProperty,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        PageRequest pageRequest = PageRequestUtils.getPageRequest(page, size, sortProperty, ascending);
        Page<OrderResponseDto> orderDetails = orderService.getAllOrderDetails(pageRequest, username, restaurantName, orderType, status);
        return ResponseEntity.ok(orderDetails);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable UUID orderId,
                                         @AuthenticationPrincipal UserDetailsImpl user) {
        Order order = orderService.getOrder(orderId);
        orderService.deleteOrder(order, user.getUser());
        return ResponseEntity.ok().body(new MessageResponseDto("Order" + SuccessMessage.DELETE.getMessage()));
    }
}
