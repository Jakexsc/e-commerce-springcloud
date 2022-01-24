package com.xsc.commerce.strem.xsc;

import com.alibaba.fastjson.JSON;
import com.xsc.commerce.vo.XscMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Slf4j
@EnableBinding(XscSource.class)
public class XscSendService {
    @Resource
    private XscSource xscSource;

    public void sendMessage(XscMessage xscMessage) {
        String message = JSON.toJSONString(xscMessage);
        log.info("in xscSendService send message: [{}]", message);
        xscSource.xscOutput().send(MessageBuilder.withPayload(message).build());
    }
}
