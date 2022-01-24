package com.xsc.commerce.strem.xsc;

import com.alibaba.fastjson.JSON;
import com.xsc.commerce.vo.XscMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Slf4j
@EnableBinding(XscSink.class)
public class XscReceiveService {

    @StreamListener(XscSink.INPUT)
    public void receiveMessage(@Payload Object payload) {
        log.info("in XscReceiveService consume message start");
        XscMessage xscMessage = JSON.parseObject(payload.toString(), XscMessage.class);
        log.info("in XscReceiveService consume message success: [{}]",
                JSON.toJSONString(xscMessage));
    }
}
