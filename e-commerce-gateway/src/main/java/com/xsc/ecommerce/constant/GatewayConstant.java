package com.xsc.ecommerce.constant;

/**
 * @author Jakexsc
 * 2022/1/3
 */
public class GatewayConstant {
    public static final String LOGIN_URI = "/e-commerce/login";

    public static final String REGISTER_URI = "/e-commerce/register";

    public static final String AUTHORITY_CENTER_TOKEN_URI_FORMAT =
            "http://%s:%s/ecommerce-authority-center/authority/token";

    public static final String AUTHORITY_CENTER_REGISTER_URI_FORMAT =
            "http://%s:%s/ecommerce-authority-center/authority/register";
}
