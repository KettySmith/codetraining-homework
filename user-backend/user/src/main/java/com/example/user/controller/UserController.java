package com.example.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    /**
     * Created on 2024/8/14
     * Description: 获取用户列表
     *
     * @param searchContent
     * @param pageSize
     * @param pageNum
     * @return ResponseVO<Map<String,Object>>
     * @author wangjiahui
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "searchContent", value = "检索关键词", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "单页元素数目", dataType = "int")
    })
    @GetMapping(value = "/getUserList")
    public ResponseVO<List<Map<String, Object>>> getUserList(@RequestParam(value = "searchContent",required = false,defaultValue = "")String searchContent,
                                                       @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                                       @RequestParam(value = "pageSize",required = false)Integer pageSize) {
        System.out.println("@@@"+searchContent);
        System.out.println("@@@"+pageNum);
        System.out.println("@@@"+pageSize);
        return ResponseVO.success(userService.getUserList(searchContent,pageNum,pageSize));

    }




}
