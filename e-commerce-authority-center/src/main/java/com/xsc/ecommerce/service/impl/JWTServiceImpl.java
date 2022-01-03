package com.xsc.ecommerce.service.impl;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.xsc.ecommerce.constans.CommonConstant;
import com.xsc.ecommerce.constant.AuthorityConstant;
import com.xsc.ecommerce.dao.EcommerceUserDao;
import com.xsc.ecommerce.entity.EcommerceUser;
import com.xsc.ecommerce.service.IJWTService;
import com.xsc.ecommerce.vo.LoginUserInfo;
import com.xsc.ecommerce.vo.UsernameAndPassword;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jakexsc
 * 2021/12/27
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class JWTServiceImpl implements IJWTService {
    @Resource
    private EcommerceUserDao ecommerceUserDao;

    @Override
    public String generateToken(String username, String password) throws Exception {
        return generateToken(username, password, 0);
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {
        // 首先验证用户是否能够通过授权校验，输入的用户名和密码是否有误
        EcommerceUser ecommerceUser = ecommerceUserDao.findByUsernameAndPassword(username, password);
        if (ecommerceUser == null) {
            log.error("can not find user: [{}], [{}]", username, password);
            return null;
        }
        // Token 塞入对象
        LoginUserInfo loginUserInfo = new LoginUserInfo(ecommerceUser.getId(), ecommerceUser.getUsername());
        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_TIME_DAY;
        }
        // 计算超时时间
        ZonedDateTime zdt = LocalDate.now().plus(expire, ChronoUnit.DAYS)
                .atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zdt.toInstant());
        return Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo))
                // jwt id
                .setId(UUID.randomUUID().toString())
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception {
        // 判断用户名是否存在
        EcommerceUser oldUser = ecommerceUserDao.findByUsername(usernameAndPassword.getUsername());
        if (oldUser != null) {
            log.error("username is register: [{}]", usernameAndPassword.getUsername());
            return null;
        }
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername(usernameAndPassword.getUsername());
        ecommerceUser.setPassword(usernameAndPassword.getPassword());
        ecommerceUser.setExtraInfo("{}");
        ecommerceUser = ecommerceUserDao.save(ecommerceUser);
        log.info("register user success: [{}]", ecommerceUser);
        return generateToken(ecommerceUser.getUsername(), ecommerceUser.getPassword());
    }

    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY)
        );
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(priPKCS8);
    }
}
