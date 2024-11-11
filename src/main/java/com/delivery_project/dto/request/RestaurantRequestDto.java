package com.delivery_project.dto.request;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class RestaurantRequestDto {

    private String name;
    private UUID categoryId;
    private UUID ownerId;
    private String address;
    private Boolean isHidden;
}
