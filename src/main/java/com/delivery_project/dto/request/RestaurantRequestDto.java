package com.delivery_project.dto.request;

import java.util.UUID;
import lombok.Getter;

@Getter
public class RestaurantRequestDto {
    private String name;
    private UUID categoryId;
    private UUID ownerId;
    private String address;
    private boolean isHidden;
}
