package com.example.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.exceptions.CustomRuntimeException;
import com.example.user.dto.UserDTO;
import com.example.user.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {


}
