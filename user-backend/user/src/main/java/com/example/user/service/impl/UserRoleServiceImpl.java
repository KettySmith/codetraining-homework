package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.user.entity.Role;
import com.example.user.entity.UserRole;
import com.example.user.mapper.RoleMapper;
import com.example.user.mapper.UserRoleMapper;
import com.example.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Autowired
    public UserRoleServiceImpl(RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public void addUserRole(Long userId, List<Integer> roleIdList, boolean deleteOldRole) {
        if (deleteOldRole) {
            removeByMap(new HashMap<String, Object>() {{
                put("user_id", userId);
            }});
        }
        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }
        List<Role> roleList = roleMapper.selectBatchIds(roleIdList);
        Set<Integer> roleIdSet = roleList.stream().collect(HashSet::new, (set, role) -> set.add(role.getId()), HashSet::addAll);
        if (roleIdList.stream().anyMatch(roleId -> !roleIdSet.contains(roleId))) {
            throw new IllegalArgumentException("roleIdList中包含无效的roleId");
        }
        List<UserRole> userRoleList = new ArrayList<>();
        Date date = new Date();
        for (Integer roleId : roleIdList) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId).setRoleId(roleId).setCreateTime(date);
            userRoleList.add(userRole);
        }
        saveBatch(userRoleList, userRoleList.size());
    }
}
