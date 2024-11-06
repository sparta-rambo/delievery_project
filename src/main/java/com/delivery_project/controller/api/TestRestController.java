package com.delivery_project.controller.api;

import com.delivery_project.dto.request.TestRequestDto;
import com.delivery_project.entity.TestEntity;
import com.delivery_project.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestRestController {

    private final TestService testService;

    @PostMapping
    public ResponseEntity<?> createTest(@RequestBody TestRequestDto.Create testRequestDto) {
        testService.save(new TestEntity(testRequestDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
