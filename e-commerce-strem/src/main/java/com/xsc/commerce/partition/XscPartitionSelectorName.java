package com.xsc.commerce.partition;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Slf4j
@Component
public class XscPartitionSelectorName implements PartitionSelectorStrategy {
    @Override
    public int selectPartition(Object key, int partitionCount) {
        int partition = key.toString().hashCode() % partitionCount;
        log.info("stream selector info: [{}], [{}], [{}]", JSON.toJSONString(key), partitionCount, partition);
        return partition;
    }
}
