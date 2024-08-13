package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.constant.PermissionCode;
import com.example.common.enums.CommonStatusEnum;
import com.example.common.enums.StorageEnum;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.common.utils.FileUtils;
import com.example.common.utils.ServletUtils;
import com.example.common.vo.ResponseVO;
import com.example.user.constant.ApiConstants;
import com.example.user.dto.UserDTO;
import com.example.user.dto.UserSelfDTO;
import com.example.user.entity.User;
import com.example.user.enums.StatusEnum;
import com.example.user.service.UserRoleService;
import com.example.user.service.UserService;
import com.example.user.utils.DataUtils;
import com.example.user.utils.UserUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@Api(tags = "user-api")
@RequestMapping(ApiConstants.API_PREFIX + "/users")
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    /**
     * Created on 2024/8/13
     * Description: 获取用户列表
     *
     * @param searchContent
     * @param pageNum
     * @param pageSize
     * @return ResponseVO<Map<String,Object>>
     * @author wangjiahui
     */
    @ApiOperation(value = "获取用户列表")
    @PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name="searchContent", value="检索词", dataType="String"),
            @ApiImplicitParam(name = "pageNum", value = "第几页", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页显示数目", paramType = "query", required = true, dataType = "int")
    })
    @PostMapping("/getUserList")
    public ResponseVO<List<Map<String, Object>>> getUserList(
            @RequestParam(value = "searchContent", defaultValue = "") String searchContent,
            @RequestParam(value = "pageNum", required = true) Integer pageNum,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) {


        return ResponseVO.success(userService.getUserList(searchContent,pageNum,pageSize));
    }
}
