package com.delivery_project.exception.category;

import com.delivery_project.dto.response.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionHandler {

  @ExceptionHandler(DuplicateCategoryNameException.class)
  public ResponseEntity<MessageResponseDto> handleDuplicateCategoryName(DuplicateCategoryNameException e) {
    return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
  }

  @ExceptionHandler(InvalidCategoryNameException.class)
  public ResponseEntity<MessageResponseDto> handleInvalidCategoryNameException(InvalidCategoryNameException e) {
    return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
  }

  @ExceptionHandler(NoCategoryFoundException.class)
  public ResponseEntity<MessageResponseDto> handleNoCategoryFoundException(NoCategoryFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponseDto(e.getMessage()));
  }
}