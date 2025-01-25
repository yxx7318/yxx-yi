package com.yxx.example.service;

import com.yxx.common.core.domain.R;
import com.yxx.example.domain.TestUserEntity;

import java.util.List;

public interface TestUserService {

    R<List<TestUserEntity>> userList();

    R<TestUserEntity> getUser(Integer userId);

    R<String> save(TestUserEntity user);

    R<String> update(TestUserEntity user);

    R<String> delete(Integer userId);
}
