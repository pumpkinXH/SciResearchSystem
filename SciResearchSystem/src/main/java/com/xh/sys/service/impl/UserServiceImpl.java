package com.xh.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xh.common.utils.JwtUtil;
import com.xh.sys.entity.Menu;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    //@Autowired
    //private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private MenuServiceImpl menuService;

//    @Override
//    public Map<String, Object> login(User user) {
//        // 根据用户名和密码查询（未做加密处理)
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        wrapper.eq(User::getPassword, user.getPassword());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//        //结果不为空，则生成token，并将用户信息存入redis
//        if(loginUser!=null){
//            //暂时方案用UUID，最终用jwt
//            String key = "user:" + UUID.randomUUID();
//            //存入redis（token格式）
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES); //存入30分钟
//            //返回数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("token",key);
//            return data;
//        }
//        return null;
//    }

//    @Override
//    public Map<String, Object> login(User user) { //做加密处理
//        // 根据用户名查询
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        User loginUser = this.baseMapper.selectOne(wrapper);
//        //结果不为空，并且密码和传入的密码匹配，则生成token，并将用户信息存入redis
//        if(loginUser!=null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){ //UUID型
//            //暂时方案用UUID，最终用jwt
//            String key = "user:" + UUID.randomUUID();
//            //存入redis（token格式）
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES); //存入30分钟
//            //返回数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("token",key);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> login(User user) { //做加密处理
        // 根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User loginUser = this.baseMapper.selectOne(wrapper);
        //结果不为空，并且密码和传入的密码匹配，则使用jwt生成token
        if(loginUser!=null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){  //用jwt且不存redis
            loginUser.setPassword(null);

            //创建jwt
            String token = jwtUtil.createToken(loginUser);

            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> cardLogin(User user) { //卡号登录
        // 根据卡号查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getCard, user.getCard());
        User loginUser = this.baseMapper.selectOne(wrapper);
        //结果不为空，并且密码和传入的密码匹配，则使用jwt生成token
        if(loginUser!=null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){  //用jwt且不存redis
            loginUser.setPassword(null);

            //创建jwt
            String token = jwtUtil.createToken(loginUser);

            //返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }

//    @Override
//    public Map<String, Object> getUserInfo(String token) {
//        //根据token获取从redis中用户信息
//        Object obj = redisTemplate.opsForValue().get(token);
//        if(obj!=null){
//            //fastjson反序列化
//            User loginUser = JSON.parseObject(JSON.toJSONString(obj), User.class);
//            Map<String, Object> data = new HashMap<>();
//            data.put("name",loginUser.getUsername());
//            data.put("avatar", loginUser.getUserAvatar());
//            //角色
//            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getUserId());
//            data.put("roles",roleList);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> getUserInfo(String token) {  //登录验证通过后根据token获取用户信息
        //使用jwt获取token
        User loginUser = null;
        try {
            loginUser =  jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(loginUser!=null){
            Map<String, Object> data = new HashMap<>();
            data.put("id",loginUser.getUserId());
            data.put("name",loginUser.getUsername());
            data.put("avatar", loginUser.getUserAvatar());
            //角色
            data.put("role",this.baseMapper.getRoleDescByUserId(loginUser.getUserId()));
            //权限菜单列表
            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getUserId());
            data.put("menuList",menuList);

            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        //redisTemplate.delete(token);
    }

    @Override
    public User getUserById(Integer userId) { //根据id查用户所有数据
        User user = this.baseMapper.selectById(userId);
        user.setRoleDesc(userMapper.getRoleDescByUserId(userId));
        user.setSchoolDesc(userMapper.getSchoolDescByUserId(userId));
        return user;
    }
}
