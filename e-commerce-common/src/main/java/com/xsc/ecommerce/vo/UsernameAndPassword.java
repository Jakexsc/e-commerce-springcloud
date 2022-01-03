package com.xsc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和密码
 *
 * @author Jakexsc
 * 2021/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameAndPassword {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
