package com.xsc.ecommerce.service;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.xsc.ecommerce.dao.EcommerceUserDao;
import com.xsc.ecommerce.entity.EcommerceUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * EcommerceUser相关的测试
 *
 * @author Jakexsc
 * 2021/12/26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class EcommerceUserTests {
    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void createUserRecord() {
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("649199985@qq.com");
        ecommerceUser.setPassword(MD5.create().digestHex("123456"));
        ecommerceUser.setExtraInfo("{}");
        log.info("save ecommerceUser: [{}]", JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));
    }
}
