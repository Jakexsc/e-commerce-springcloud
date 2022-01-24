package com.xsc.commerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XscMessage {
    private Integer id;
    private String projectName;
    private String org;
    private String author;
    private String version;

    /**
     * <h2>返回一个默认的消息, 方便使用</h2>
     * */
    public static XscMessage defaultMessage() {

        return new XscMessage(
                1,
                "e-commerce-stream-client",
                "imooc.com",
                "Qinyi",
                "1.0"
        );
    }
}
