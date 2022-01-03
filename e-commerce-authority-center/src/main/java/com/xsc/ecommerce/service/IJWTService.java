package com.xsc.ecommerce.service;

import com.xsc.ecommerce.vo.UsernameAndPassword;

/**
 * 相关服务接口定义
 */
public interface IJWTService {
    /**
     * 生成JWT token, 使用默认的超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @return String JWT Token
     * @throws Exception
     */
    String generateToken(String username, String password) throws Exception;

    /**
     * 生成JWT token, 指定超时时间
     *
     * @param username 用户名
     * @param password 密码
     * @return String JWT Token
     * @throws Exception
     */
    String generateToken(String username, String password, int expire) throws Exception;

    /**
     * 注册并生成JWT token
     *
     * @param usernameAndPassword 注册信息
     * @return String
     * @throws Exception
     */
    String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception;
}
