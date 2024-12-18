package com.xh.config;

import com.xh.interceptor.JwtValidateInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtValidateInterceptor jwtValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) { //重写函数
        InterceptorRegistration registration = registry.addInterceptor(jwtValidateInterceptor);
        registration.addPathPatterns("/**") //拦截所有
                    .excludePathPatterns( //排除，下述放行
                            "/user",    //注册时的新增
                            "/user/login",
                            "/user/cardLogin",
                            "/user/info",
                            "/user/logout",
                            "/error",
                            "/school/allExCo",  //注册时的查询学院
                            //swagger相关
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/v3/**",
                            "/files/**",  //文件上传相关
                            "/pro/exportExcel",  //导出项目
                            "/res/exportExcel"  //导出成果
                    );
    }
}
