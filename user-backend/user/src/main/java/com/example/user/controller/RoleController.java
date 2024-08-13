package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.common.utils.ServletUtils;
import com.example.common.vo.ResponseVO;
import com.example.user.constant.ApiConstants;
import com.example.user.dto.RoleDTO;
import com.example.user.entity.Permission;
import com.example.user.entity.Role;
import com.example.user.entity.RolePermission;
import com.example.user.enums.StatusEnum;
import com.example.user.service.PermissionService;
import com.example.user.service.RolePermissionService;
import com.example.user.service.RoleService;
import com.example.user.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "role-api")
@RequestMapping(ApiConstants.API_PREFIX + "/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    @Autowired
    public RoleController(RoleService roleService, UserRoleService userRoleService,
                RolePermissionService rolePermissionService, PermissionService permissionService) {
            this.roleService = roleService;
            this.userRoleService = userRoleService;
            this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }



}
