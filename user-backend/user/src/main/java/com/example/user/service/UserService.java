package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.user.dto.UserDTO;
import com.example.user.entity.User;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    List<Map<String, Object>> getUserRoleAndPermissionsByUserId(List<Long> userIds);

    Page<User> getUserList(String searchContent, Integer pageNum, Integer pageSize);

    User addOneUser(UserDTO userDTO) throws CustomRuntimeException;
    User addUser(String userName, String trueName, String password, String email, Integer gender, String address, String introduction, String phone, List<Integer> roleIds);

    boolean existsByUserName(String userName, Boolean throwExceptionWhenExists) throws CustomRuntimeException;

}
