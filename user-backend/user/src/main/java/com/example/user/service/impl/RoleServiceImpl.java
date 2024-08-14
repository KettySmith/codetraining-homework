package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.user.entity.Role;
import com.example.user.enums.StatusEnum;
import com.example.user.mapper.RoleMapper;
import com.example.user.service.RoleService;
import com.example.user.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    @Override
    public List<Map<String, Object>> getRoles() {
        return roleMapper.getRoles();
    }
}
