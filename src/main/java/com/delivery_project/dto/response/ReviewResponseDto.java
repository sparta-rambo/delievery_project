package com.delivery_project.dto.response;

import com.delivery_project.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    public Integer rating;
    public String comment;

    public ReviewResponseDto(Review review) {
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}
