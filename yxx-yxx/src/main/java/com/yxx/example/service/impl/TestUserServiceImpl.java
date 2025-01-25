package com.yxx.example.service.impl;

import com.yxx.common.core.domain.R;
import com.yxx.common.utils.StringUtils;
import com.yxx.example.domain.TestUserEntity;
import com.yxx.example.mapper.TestUserMapper;
import com.yxx.example.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestUserServiceImpl implements TestUserService {

    private final static Map<Integer, TestUserEntity> users = new LinkedHashMap<Integer, TestUserEntity>();

    {
        users.put(1, new TestUserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new TestUserEntity(2, "ry", "admin123", "15666666666"));
    }

    @Autowired
    private TestUserMapper testUserMapper;

    @Override
    public R<List<TestUserEntity>> userList() {
        return R.ok(new ArrayList<>(users.values()));
    }

    @Override
    public R<TestUserEntity> getUser(Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return R.ok(users.get(userId));
        } else {
            return R.fail("用户不存在");
        }
    }

    @Override
    public R<String> save(TestUserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @Override
    public R<String> update(TestUserEntity user) {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())) {
            return R.fail("用户不存在");
        }
        users.remove(user.getUserId());
        users.put(user.getUserId(), user);
        return R.ok();
    }

    @Override
    public R<String> delete(Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return R.ok();
        } else {
            return R.fail("用户不存在");
        }
    }
}
