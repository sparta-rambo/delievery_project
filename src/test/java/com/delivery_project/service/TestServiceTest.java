package com.delivery_project.service;

import com.delivery_project.entity.TestEntity;
import com.delivery_project.repository.jpa.TestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestServiceTest {

    @Autowired
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @Test
    void save() {
    }

    @Test
    void getById() {
        //given
        TestEntity testEntity = TestEntity.builder()
                .role("ROLE_USER")
                .username("test")
                .password("test")
                .id(1)
                .build();
        testRepository.save(testEntity);

        //when
        TestEntity testEntity1 = testService.getById(1);

        //then
        Assertions.assertThat(testEntity1).isEqualTo(testEntity);
        System.out.println(testEntity1.getUsername());
    }
}