package com.xh.interceptor;

import com.alibaba.fastjson2.JSON;
import com.xh.common.utils.JwtUtil;
import com.xh.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component  // 标注一个类为Spring容器的Bean
@Slf4j //打印日志信息
public class JwtValidateInterceptor implements HandlerInterceptor {  //Jwt拦截器
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { //重写方法
        String token =  request.getHeader("X-token");
        log.debug(request.getRequestURI() + "需要验证：" + token);
        if(token!=null){
            try {
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI() + "验证通过");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI() + "验证失败，禁止访问");
        response.setContentType("application/json;charset=utf-8");
        Result<Object> fail = Result.fail(20003,"jwt无效，请重新登录");  //返回给前端提示信息
        response.getWriter().write(JSON.toJSONString(fail));
        return false; //拦截
    }
}
