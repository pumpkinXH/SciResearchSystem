package com.xh.sys.service;

import com.xh.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
public interface IUserService extends IService<User> {
    Map<String, Object> login(User user);

    Map<String, Object> cardLogin(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);

    User getUserById(Integer userId);
}
