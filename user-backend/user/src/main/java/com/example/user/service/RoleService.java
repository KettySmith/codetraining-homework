package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.user.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService extends IService<Role> {


    List<Map<String, Object>> getRoles();
}
