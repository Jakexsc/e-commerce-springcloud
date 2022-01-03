package com.xsc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登陆用户信息
 *
 * @author Jakexsc
 * 2021/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {
    /**
     * 主键Id
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
}
