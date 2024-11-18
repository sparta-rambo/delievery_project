package com.delivery_project.common;

import com.delivery_project.entity.Order;
import com.delivery_project.security.UserDetailsImpl;
import org.springframework.stereotype.Component;

@Component
public class PermissionHandler {

    public boolean canAccessOrder(Order order, UserDetailsImpl userDetails) {
        // 사용자와 주문의 관계를 검증
        if (order == null || userDetails == null) {
            return false; // 유효하지 않은 요청은 거부
        }

        // 예: 사용자 ID가 주문 소유자 ID와 동일한지 확인
        return order.getUser().getId().equals(userDetails.getUser().getId());
    }

}
