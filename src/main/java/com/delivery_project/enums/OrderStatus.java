package com.delivery_project.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CANCEL("취소"),
    CONFIRM("주문 조회 중"),
    DELIVERY("배달 중"),
    FINISHED("배달 완료");

    private final String status;

}
