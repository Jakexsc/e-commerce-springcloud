package com.xsc.ecommerce.service;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author Jakexsc
 * 2021/12/26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RSATest {
    @Test
    public void generateKeyBytes() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey pairPublic = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey pairPrivate = (RSAPrivateKey) keyPair.getPrivate();
        log.info("public Key: [{}]", Base64.encode(pairPublic.getEncoded()));
        log.info("private key: [{}]", Base64.encode(pairPrivate.getEncoded()));
    }
}
