package com.xh.sys.mapper;

import com.xh.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface UserMapper extends BaseMapper<User> {
    String getRoleDescByUserId(Integer userId);

    String getSchoolDescByUserId(Integer userId);

    void updateKeyInfoByUserId(Integer userId, String username, String password, String newPassword);

    String getPasswordByUserId(Integer userId);

    @Select("select * from user where user_id != 1")
    List<User> getAllUserExceptAdmin();

    @Select("select * from user where user_role_id = 4")
    List<User> getAllSciUser();

    @Select("select card from user where card is not null and deleted = 0 order by user_id desc limit 1")
    String getNewCard();
}
