package com.delivery_project.controller.api;

import com.delivery_project.dto.request.TestRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.enums.SuccessMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestRestController {

    @PostMapping("/test")
    public ResponseEntity<?> createTest(@RequestBody TestRequestDto.Create testRequestDto) {
//        testService.save(new TestEntity(testRequestDto));
        return ResponseEntity.ok().body(new MessageResponseDto("test" + SuccessMessage.CREATE.getMessage()));
    }

}
