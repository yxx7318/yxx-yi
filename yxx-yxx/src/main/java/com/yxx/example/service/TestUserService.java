package com.yxx.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.common.core.domain.R;
import com.yxx.example.domain.TestUserEntity;

import java.util.List;

public interface TestUserService extends IService<TestUserEntity> {

    R<List<TestUserEntity>> userList();

    R<TestUserEntity> getUser(Integer userId);

    R<String> saveUser(TestUserEntity user);

    R<String> updateUser(TestUserEntity user);

    R<String> deleteUser(Integer userId);
}
