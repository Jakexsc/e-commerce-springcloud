package com.xsc.commerce.controller;

import com.xsc.commerce.strem.xsc.XscSendService;
import com.xsc.commerce.vo.XscMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Jakexsc
 * 2022/1/25
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private XscSendService xscSendService;

    @GetMapping("/xsc")
    public void xscSend() {
        xscSendService.sendMessage(XscMessage.defaultMessage());
    }
}
