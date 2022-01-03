package com.xsc.ecommerce.controller;

import com.alibaba.fastjson.JSON;
import com.xsc.ecommerce.annotation.IgnoreResponseAdvice;
import com.xsc.ecommerce.service.IJWTService;
import com.xsc.ecommerce.vo.JwtToken;
import com.xsc.ecommerce.vo.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jakexsc
 * 2021/12/27
 */
@Slf4j
@RestController
@RequestMapping(value = "/authority")
public class AuthorityController {
    @Autowired
    private IJWTService jwtService;

    @IgnoreResponseAdvice
    @PostMapping("/token")
    public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        log.info("request to get token with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(jwtService.generateToken(
                        usernameAndPassword.getUsername(),
                        usernameAndPassword.getPassword(),
                        1));
    }

    @IgnoreResponseAdvice
    @PostMapping("/register")
    public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        log.info("register user with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(jwtService.registerUserAndGenerateToken(usernameAndPassword));
    }
}
