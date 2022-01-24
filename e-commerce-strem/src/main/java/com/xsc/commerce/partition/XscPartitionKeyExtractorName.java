package com.xsc.commerce.partition;

import com.alibaba.fastjson.JSON;
import com.xsc.commerce.vo.XscMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Slf4j
@Component
public class XscPartitionKeyExtractorName implements PartitionKeyExtractorStrategy {
    @Override
    public Object extractKey(Message<?> message) {
        XscMessage xscMessage = JSON.parseObject(message.getPayload().toString(), XscMessage.class);
        String key = xscMessage.getProjectName();
        log.info("partition key: [{}]", key);
        return key;
    }
}
