package com.xsc.commerce.strem.xsc;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author Jakexsc
 * 2022/1/25
 */
public interface XscSource {
    String OUTPUT = "xscOutput";

    @Output(XscSource.OUTPUT)
    MessageChannel xscOutput();
}
