package com.delivery_project.dto.response;

import java.util.UUID;
import lombok.Getter;

@Getter
public class RestaurantResponseDto {
    private UUID id;
    private String name;
    private UUID categoryId;
    private UUID ownerId;
    private String address;
    private boolean isHidden;

}
