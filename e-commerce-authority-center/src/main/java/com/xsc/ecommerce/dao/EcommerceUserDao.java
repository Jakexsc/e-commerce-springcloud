package com.xsc.ecommerce.dao;

import com.xsc.ecommerce.entity.EcommerceUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcommerceUserDao extends JpaRepository<EcommerceUser, Long> {
    /**
     * 根据用户名查询ecommerceUser对象
     *
     * @param username 用户名
     * @return EcommerceUser
     */
    EcommerceUser findByUsername(String username);

    /**
     * 根据用户名和密码查询ecommerceUser对象
     *
     * @param username 用户名
     * @param password 密码
     * @return EcommerceUser
     */
    EcommerceUser findByUsernameAndPassword(String username, String password);
}
