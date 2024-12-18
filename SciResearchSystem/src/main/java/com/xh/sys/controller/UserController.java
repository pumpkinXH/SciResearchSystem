package com.xh.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xh.common.vo.Result;
import com.xh.sys.entity.User;
import com.xh.sys.mapper.UserMapper;
import com.xh.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xh
 * @since 2023-04-05
 */
@Api(tags = {"用户接口"})  //Swagger文档注解
@RestController
@RequestMapping("/user")
//@CrossOrigin //跨域处理
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @ApiOperation("查询所有用户数据")  //Swagger文档注解
    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> list = userService.list();  //数据
        return Result.success(list,"查询成功");
    }

    @ApiOperation("查询所有科研人员数据")  //Swagger文档注解
    @GetMapping("/allSci")
    public Result<List<User>> getAllSciUser(){
        List<User> list = userMapper.getAllSciUser();  //数据
        return Result.success(list,"查询成功");
    }

    @ApiOperation("用户登录")  //Swagger文档注解
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user){
        Map<String, Object> data =  userService.login(user);
        if(data!=null){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @ApiOperation("用户卡号登录")  //Swagger文档注解
    @PostMapping("/cardLogin")
    public Result<Map<String, Object>> cardLogin(@RequestBody User user){
        Map<String, Object> data =  userService.cardLogin(user);
        if(data!=null){
            return Result.success(data);
        }
        return Result.fail(20002,"卡号或密码错误");
    }

    @ApiOperation("判断登录信息是否有效")  //Swagger文档注解
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token){ //验证通过后获取用户信息
        //根据jwt的token获取用户信息
        Map<String, Object> data = userService.getUserInfo(token);
        if(data!=null){
            return Result.success(data);
        }
        return Result.fail(20003,"登陆信息无效，请重新登陆");
    }

    @ApiOperation("个人信息查询接口")  //Swagger文档注解
    @GetMapping("/personInfo")
    public Result<User> getPersonInfo(@RequestParam("token") String token){ //验证通过后获取用户信息
        //根据jwt的token获取用户信息
        Map<String, Object> data = userService.getUserInfo(token);
        Integer userId = (Integer) data.get("id");
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    @ApiOperation("用户注销")  //Swagger文档注解
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    @ApiOperation("用户信息列表显示")  //Swagger文档注解
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                                   @RequestParam(value = "card",required = false) String card,
                                                   @RequestParam(value = "userSchoolId",required = false) String userSchoolId,
                                                   @RequestParam(value = "userRoleId",required = false) String userRoleId,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize
                                                   ){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(username),User::getUsername, username); //模糊查询，传入的字符串长度大于0再做拼接
        wrapper.like(StringUtils.hasLength(card),User::getCard, card);
        wrapper.eq(StringUtils.hasLength(userSchoolId),User::getUserSchoolId, userSchoolId);
        wrapper.eq(StringUtils.hasLength(userRoleId),User::getUserRoleId, userRoleId);
        wrapper.ne(User::getUserId,1);
        // wrapper.orderByDesc(User::getUserId); 倒序排序
        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);
        page.getRecords().forEach(index -> {
            index.setRoleDesc(userMapper.getRoleDescByUserId(index.getUserId()));  //角色
            index.setSchoolDesc(userMapper.getSchoolDescByUserId(index.getUserId()));//学院
        });
        Map<String, Object> data = new HashMap<>();
//        if(StringUtils.hasLength(schoolDesc)){  //根据学院模糊查询（输入框时）
//            List<User> userList = page.getRecords().stream().filter(index ->
//                    index.getSchoolDesc().contains(schoolDesc)).collect(Collectors.toList());
//            data.put("total",userList.size());
//            data.put("rows",userList);
//        }else {
//            data.put("total",page.getTotal());
//            data.put("rows",page.getRecords());
//        }
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("卡号分配信息列表显示")  //Swagger文档注解
    @GetMapping("/cardList")
    public Result<Map<String, Object>> getCardList(@RequestParam(value = "username",required = false) String username,
                                                   @RequestParam(value = "card",required = false) String card,
                                                   @RequestParam(value = "userSchoolId",required = false) String userSchoolId,
                                                   @RequestParam(value = "userRoleId",required = false) String userRoleId,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize
    ){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(User::getCard);  //卡号为空的数据
        wrapper.like(StringUtils.hasLength(username),User::getUsername, username); //模糊查询，传入的字符串长度大于0再做拼接
        wrapper.like(StringUtils.hasLength(card),User::getCard, card);
        wrapper.eq(StringUtils.hasLength(userSchoolId),User::getUserSchoolId, userSchoolId);
        wrapper.eq(StringUtils.hasLength(userRoleId),User::getUserRoleId, userRoleId);
        wrapper.ne(User::getUserId,1);
        // wrapper.orderByDesc(User::getUserId); 倒序排序
        Page<User> page = new Page<>(pageNo,pageSize);
        userService.page(page,wrapper);
        page.getRecords().forEach(index -> {
            index.setRoleDesc(userMapper.getRoleDescByUserId(index.getUserId()));  //角色
            index.setSchoolDesc(userMapper.getSchoolDescByUserId(index.getUserId()));//学院
        });
        Map<String, Object> data = new HashMap<>();
//        if(StringUtils.hasLength(schoolDesc)){  //根据学院模糊查询（输入框时）
//            List<User> userList = page.getRecords().stream().filter(index ->
//                    index.getSchoolDesc().contains(schoolDesc)).collect(Collectors.toList());
//            data.put("total",userList.size());
//            data.put("rows",userList);
//        }else {
//            data.put("total",page.getTotal());
//            data.put("rows",page.getRecords());
//        }
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());
        return Result.success(data);
    }

    @ApiOperation("新增用户")  //Swagger文档注解
    @PostMapping
    public Result<?> addUser(@RequestBody User user){ //json转Request对象
        LambdaQueryWrapper<User> wrapperCard = new LambdaQueryWrapper<>();
        wrapperCard.eq(User::getCard, user.getCard());
        if(userMapper.selectCount(wrapperCard)>0){
            return Result.fail(20004,"卡号重复");
        }
        LambdaQueryWrapper<User> wrapperUsername = new LambdaQueryWrapper<>();
        wrapperUsername.eq(User::getUsername, user.getUsername());
        if(userMapper.selectCount(wrapperUsername)>0){
            return Result.fail(20005,"用户名重复");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));  //密码加密处理
        userMapper.insert(user);
        return Result.success("新增用户成功");
    }

    @ApiOperation("修改用户信息")  //Swagger文档注解
    @PutMapping
    public Result<?> updateUser(@RequestBody User user){ //修改
        user.setPassword(null);  //此处不可修改密码
        userMapper.updateById(user);//密码传入为空，则代表不会更新密码
        return Result.success("修改用户信息成功");
    }

    @ApiOperation("修改用户名和密码接口")  //Swagger文档注解
    @PutMapping("/keyInfoChange")
    public Result<?> updateKeyInfo(@RequestBody User user){ //验证通过后获取用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getCard, user.getCard());
        if(userMapper.selectCount(wrapper)!=1){
            LambdaQueryWrapper<User> wrapperUsername = new LambdaQueryWrapper<>();
            wrapperUsername.eq(User::getUsername, user.getUsername());
            if(userMapper.selectCount(wrapperUsername)>0){
                return Result.fail(20005,"用户名重复");
            }
        }
        String oldPassword = userMapper.getPasswordByUserId(user.getUserId());  //数据库存的原密码（已加密）
        if(!passwordEncoder.matches((user.getPassword()),oldPassword)) return Result.fail(20006,"原密码错误");
        String newPassword = passwordEncoder.encode(user.getNewPassword());  //新密码加密
        userMapper.updateKeyInfoByUserId(user.getUserId(),user.getUsername(),oldPassword,newPassword);
        return Result.success("用户名和密码修改成功，请返回重新登录");
    }

    @ApiOperation("根据用户id查询")  //Swagger文档注解
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer userId){
        User user = userService.getUserById(userId);
        return Result.success(user);
    }

    @ApiOperation("根据用户id删除")  //Swagger文档注解
    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable("id") Integer userId){
        userMapper.deleteById(userId);
        return Result.success("删除用户成功");
    }

    @ApiOperation("获取到最新的已分配卡号")  //Swagger文档注解
    @GetMapping("/newCard")
    public Result<String> getNewCard(){
        String newCard = userMapper.getNewCard();
        return Result.success(newCard,"success");
    }
}
