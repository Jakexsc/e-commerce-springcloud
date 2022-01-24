package com.xsc.commerce.strem.xsc;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface XscSink {
    String INPUT = "xscInput";

    @Input(XscSink.INPUT)
    SubscribableChannel xscInput();
}
