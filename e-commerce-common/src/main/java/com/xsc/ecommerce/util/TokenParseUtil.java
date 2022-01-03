package com.xsc.ecommerce.util;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.xsc.ecommerce.constans.CommonConstant;
import com.xsc.ecommerce.vo.LoginUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * @author Jakexsc
 * 2021/12/27
 */
@Slf4j
public class TokenParseUtil {
    /**
     * 根据公钥解析用户信息
     *
     * @param token jwt token
     * @return LoginUserInfo
     * @throws Exception
     */
    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception {
        if (null == token) {
            return null;
        }
        Jws<Claims> claimsJws = parseToken(token, getPublicKey());
        Claims body = claimsJws.getBody();
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            log.error("token已经过期: [{}]", body);
            return null;
        }
        return JSON.parseObject(body.get(CommonConstant.JWT_USER_INFO_KEY).toString(), LoginUserInfo.class);
    }

    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 生成公钥
     *
     * @return PublicKey
     * @throws Exception
     */
    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(CommonConstant.PUBLIC_KEY));
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }
}
