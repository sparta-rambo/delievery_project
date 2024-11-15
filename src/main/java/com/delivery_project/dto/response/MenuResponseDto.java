package com.delivery_project.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponseDto {

    private UUID id;
    private UUID restaurantId;
    private String name;
    private String description;
    private int price;
}