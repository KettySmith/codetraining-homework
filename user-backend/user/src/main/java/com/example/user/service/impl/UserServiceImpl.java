package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.user.dto.UserDTO;
import com.example.user.entity.User;
import com.example.user.enums.StatusEnum;
import com.example.user.mapper.UserMapper;
import com.example.user.service.UserRoleService;
import com.example.user.service.UserService;
import com.example.user.utils.DataUtils;
import com.example.user.utils.IdGenerator;
import com.example.user.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRoleService userRoleService) {
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
    }

    @Override
    public List<Map<String, Object>> getUserRoleAndPermissionsByUserId(List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        return userMapper.getUserRoleAndPermissionsByUserId(userIds);
    }

    @Override
    public List<Map<String, Object>> getUserList(String searchContent, Integer pageNum, Integer pageSize) {
        return userMapper.getUserList(searchContent, pageNum,  pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addOneUser(UserDTO userInfo) throws CustomRuntimeException {
        if (UserUtils.isUserNameContainsIllegalCharacter(userInfo.getUserName())) {
            throw new CustomRuntimeException(StatusEnum.USER_NAME_CONTAINS_ILLEGAL_CHARACTER);
        }
        // 找出是否有相同的userName
        existsByUserName(userInfo.getUserName(), true);
        User user = userInfo.toEntity(User.class);
        Date date = new Date();
        user.setId(IdGenerator.nextId());
        user.setStatus(User.Status.ENABLE).setCreateTime(date).setUpdateTime(date);
        userMapper.insert(user);
        userRoleService.addUserRole(user.getId(), userInfo.getRoleIds(), false);
        return user;
    }

    @Override
    public User addUser(String userName, String trueName, String password, String email, Integer gender, String address, String introduction, String phone, List<Integer> roleIds) {

        // 创建一个 UserDTO 对象并设置参数
        UserDTO userInfo = new UserDTO();
        userInfo.setUserName(userName);
        userInfo.setTrueName(trueName);
        userInfo.setPassword(password);
        userInfo.setEmail(email);
        userInfo.setGender(gender);
        userInfo.setAddress(address);
        userInfo.setIntroduction(introduction);
        userInfo.setPhone(phone);
        userInfo.setRoleIds(roleIds);

        // 找出是否有相同的userName
        existsByUserName(userName, true);
        User user = userInfo.toEntity(User.class);
        Date date = new Date();
        user.setId(IdGenerator.nextId());
        user.setStatus(User.Status.ENABLE).setCreateTime(date).setUpdateTime(date);
        userMapper.insert(user);
        userRoleService.addUserRole(user.getId(), userInfo.getRoleIds(), false);
        return user;    }

    @Override
    public boolean existsByUserName(String userName, Boolean throwExceptionWhenExists) throws CustomRuntimeException {
        if (DataUtils.checkEmptyString(userName)) {
            throw new CustomRuntimeException(StatusEnum.USER_NAME_NOT_EMPTY);
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_name", userName.trim()).ne("status", User.Status.DELETED));
        if (throwExceptionWhenExists == null) {
            return user != null;
        }
        if (throwExceptionWhenExists ^ user == null) {
            throw new CustomRuntimeException(user == null ? StatusEnum.USER_NAME_NOT_EXISTS : StatusEnum.USER_NAME_EXISTS);
        } else {
            return false;
        }
    }

}
