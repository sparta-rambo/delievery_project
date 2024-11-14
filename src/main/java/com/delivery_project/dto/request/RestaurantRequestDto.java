package com.delivery_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestaurantRequestDto {

    @NotBlank(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "카테고리를 선택해주세요.")
    private UUID categoryId;

    @NotNull(message = "Owner_id는 비어있을 수 없습니다.")
    private UUID ownerId;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;
}
