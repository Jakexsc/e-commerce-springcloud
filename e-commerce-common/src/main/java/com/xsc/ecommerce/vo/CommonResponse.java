package com.xsc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应对象定义
 *
 * @author Jakexsc
 * 2021/12/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public CommonResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
