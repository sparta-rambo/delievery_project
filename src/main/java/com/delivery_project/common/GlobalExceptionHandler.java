package com.delivery_project.common;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.DuplicateResourceException;
import com.delivery_project.common.exception.InvalidInputException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.response.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<MessageResponseDto> handleIllegalException(
        RuntimeException runtimeException) {
        return ResponseEntity.badRequest()
            .body(new MessageResponseDto(runtimeException.getMessage()));
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<MessageResponseDto> handleClassCastException(
        ClassCastException classCastException) {
        MessageResponseDto errorResponse = new MessageResponseDto(classCastException.getMessage());
        return ResponseEntity.ok().body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageResponseDto> handleBadRequestException(BadRequestException ex) {
        MessageResponseDto errorResponse = new MessageResponseDto(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<MessageResponseDto> handleDuplicateResourceException(DuplicateResourceException e) {
        return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<MessageResponseDto> handleInvalidInputException(InvalidInputException e) {
        return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
    }

}
