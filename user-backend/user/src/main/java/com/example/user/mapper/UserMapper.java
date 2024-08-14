package com.example.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("<script>" +
            "SELECT " +
            "    u.`id` AS userId, " +
            "    GROUP_CONCAT(DISTINCT r.id) AS roleIdList, " +
            "    GROUP_CONCAT(DISTINCT r.name) AS roleNameList, " +
            "    GROUP_CONCAT(DISTINCT p.id) AS permissionIdList, " +
            "    GROUP_CONCAT(DISTINCT p.name) AS permissionNameList, " +
            "    GROUP_CONCAT(DISTINCT p.code) AS permissionCodeList," +
            "    GROUP_CONCAT(DISTINCT p.platform) AS platformList " +
            "FROM `user` u " +
            "LEFT JOIN `user_role` ur ON u.id = ur.user_id " +
            "LEFT JOIN `role` r ON ur.role_id = r.id " +
            "LEFT JOIN `role_permission` rp ON r.id = rp.role_id " +
            "LEFT JOIN `permission` p ON rp.permission_id = p.id " +
            "WHERE u.id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach> " +
            "GROUP BY u.id" +
            "</script>")
    List<Map<String, Object>> getUserRoleAndPermissionsByUserId(@Param("userIds") List<Long> userIds);



    @Select("SELECT user_name, true_name, introduction, create_time, update_time, status  " +
            "FROM user  " +
            "WHERE user_name LIKE  CONCAT('%', #{searchContent}, '%') "  +
            "ORDER BY create_time DESC  " +
            "LIMIT  ${(pageNum - 1) * pageSize},${pageSize}")
    List<Map<String, Object>> getUserList(@Param("searchContent") String searchContent,
                                          @Param("pageNum")Integer pageNum,
                                          @Param("pageSize")  Integer pageSize);

}
