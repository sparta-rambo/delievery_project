package com.delivery_project.service;

import com.delivery_project.entity.TestEntity;
import com.delivery_project.repository.TestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public TestEntity save(TestEntity testEntity) {
        return testRepository.save(testEntity);
    }
}
