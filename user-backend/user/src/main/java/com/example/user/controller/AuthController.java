package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.enums.RoleEnum;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.common.utils.ServletUtils;
import com.example.common.vo.ResponseVO;
import com.example.user.constant.ApiConstants;
import com.example.user.dto.UserDTO;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.enums.StatusEnum;
import com.example.user.enums.PlatformEnum;
import com.example.user.service.RoleService;
import com.example.user.service.UserService;
import com.example.user.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@Api(tags = "auth-api")
@RequestMapping(ApiConstants.API_PREFIX + "/auth")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }



}

