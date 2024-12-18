package com.xh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan("com.xh.*.mapper")
public class SciResearchSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SciResearchSystemApplication.class, args);
    }

    @Bean // 密码加密的Bean
    public PasswordEncoder passwordEncoder(){ //密码加密的工具类
        return new BCryptPasswordEncoder();
    }
}