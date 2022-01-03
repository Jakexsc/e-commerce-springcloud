package com.xsc.ecommerce.service;

import com.xsc.ecommerce.util.TokenParseUtil;
import com.xsc.ecommerce.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Jakexsc
 * 2021/12/27
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class JWTServiceTests {
    @Autowired
    private IJWTService jwtService;

    @Test
    public void testGenerateAndParseToken() throws Exception {
        String token = jwtService.generateToken("649199985@qq.com", "e10adc3949ba59abbe56e057f20f883e");
        log.info("jwt token is: [{}]", token);
        LoginUserInfo loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
        log.info("loginUserInfo: [{}]", loginUserInfo);
    }
}
