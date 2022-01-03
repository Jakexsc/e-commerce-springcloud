package com.xsc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权中心鉴权之后给客户端的 Token
 *
 * @author Jakexsc
 * 2021/12/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    /**
     * JWT
     */
    private String token;
}
