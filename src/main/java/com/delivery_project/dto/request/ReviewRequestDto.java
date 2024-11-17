package com.delivery_project.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class ReviewRequestDto {

    @Getter
    public static class Create {
        private String comment;

        @NotNull(message = "별점은 필수 입니다.")
        @Min(value = 1, message = "별점은 최소 1점입니다.")
        @Max(value = 5, message = "별점은 최대 5점입니다.")
        private int rating;

        public Create(String comment, int rating) {
            this.comment = comment;
            this.rating = rating;
        }
    }
}
