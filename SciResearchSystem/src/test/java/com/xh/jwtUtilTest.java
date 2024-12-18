package com.xh;

import com.xh.common.utils.JwtUtil;
import com.xh.sys.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class jwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt(){
        User user = new User();
        user.setUsername("张三");
        user.setUserPhone("12124512512");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTg1MzA3Zi04YWY5LTQ0MDItODQ0Yi1kY2QzM2E3NGE3ZjIiLCJzdWIiOiJ7XCJ1c2VyUGhvbmVcIjpcIjEyMTI0NTEyNTEyXCIsXCJ1c2VybmFtZVwiOlwi5byg5LiJXCJ9IiwiaXNzIjoic3lzdGVtIiwiaWF0IjoxNjgwNzcwODA1LCJleHAiOjE2ODA3NzI2MDV9.7mILWDKeFXCwIiEZLPXwGv5WiuK9HkxCznd_XD9G6jY
    }

    @Test
    public void testPraseJwt(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OTg1MzA3Zi04YWY5LTQ0MDItODQ0Yi1kY2QzM2E3NGE3ZjIiLCJzdWIiOiJ7XCJ1c2VyUGhvbmVcIjpcIjEyMTI0NTEyNTEyXCIsXCJ1c2VybmFtZVwiOlwi5byg5LiJXCJ9IiwiaXNzIjoic3lzdGVtIiwiaWF0IjoxNjgwNzcwODA1LCJleHAiOjE2ODA3NzI2MDV9.7mILWDKeFXCwIiEZLPXwGv5WiuK9HkxCznd_XD9G6jY";
        //Claims claims = jwtUtil.parseToken(token);
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
