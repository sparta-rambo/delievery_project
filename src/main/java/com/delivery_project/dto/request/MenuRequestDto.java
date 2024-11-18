package com.delivery_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MenuRequestDto {

    @NotNull(message = "Restaurant_id는 비어있을 수 없습니다.")
    private UUID restaurantId;

    @NotBlank(message = "메뉴 이름을 입력해주세요.")
    private String name;

    private String description;

    @Positive(message = "가격은 양수 값이어야 합니다.")
    private int price;
}