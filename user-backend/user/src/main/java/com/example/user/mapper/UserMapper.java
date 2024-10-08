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


//    @Select("SELECT u.* , " +
//            "GROUP_CONCAT(r.name ORDER BY r.name ASC SEPARATOR ', ') as roleList  " +
//            "FROM user u  " +
//            "LEFT JOIN user_role ur ON u.id = ur.user_id  " +
//            "LEFT JOIN role r ON ur.role_id = r.id  " +
//            "WHERE u.user_name LIKE CONCAT('%', #{searchContent}, '%') "  +
//            "GROUP BY u.id " +
//            "ORDER BY u.create_time DESC  " +
//            "LIMIT ${(pageNum - 1) * pageSize}, ${pageSize}")
//    List<User> getUserList(@Param("searchContent") String searchContent,
//                                          @Param("pageNum")Integer pageNum,
//                                          @Param("pageSize")  Integer pageSize);
//
//

    @Select("SELECT * FROM user " +
            "WHERE user_name LIKE CONCAT('%', #{searchContent}, '%') " +
            "ORDER BY create_time DESC " +
            "LIMIT ${(pageNum - 1) * pageSize}, ${pageSize}")
    List<User> getUserList(@Param("searchContent") String searchContent,
                           @Param("pageNum") Integer pageNum,
                           @Param("pageSize") Integer pageSize);

    @Select("SELECT ur.user_id as userId, GROUP_CONCAT(r.name ORDER BY r.name ASC SEPARATOR ', ') as roleList " +
            "FROM user_role ur " +
            "LEFT JOIN role r ON ur.role_id = r.id " +
            "WHERE ur.user_id IN (${userIds}) " +
            "GROUP BY ur.user_id")
    List<Map<String, Object>> getUserRoleList(@Param("userIds") String userIds);


    @Select("SELECT COUNT(*) from user  " +
            "where user_name LIKE CONCAT('%', #{searchContent}, '%')")
    int countUserList(@Param("searchContent") String searchContent);
}
