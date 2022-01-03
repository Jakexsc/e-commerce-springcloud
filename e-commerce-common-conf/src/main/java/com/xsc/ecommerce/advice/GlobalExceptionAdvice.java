package com.xsc.ecommerce.advice;

import com.xsc.ecommerce.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * Jakexsc
 * 2021/12/14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(value = Exception.class)
    public CommonResponse handle(HttpServletRequest req, Exception ex) {
        CommonResponse<Object> commonResponse = new CommonResponse<>(-1, "business error");
        commonResponse.setData(ex.getMessage());
        log.error("commerce service has error: [{}]", ex.getMessage(), ex);
        return commonResponse;
    }
}
