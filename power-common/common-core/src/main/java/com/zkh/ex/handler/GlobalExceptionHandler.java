package com.zkh.ex.handler;

import com.zkh.constant.BusinessEnum;
import com.zkh.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<String> businessException(BusinessException e) {
        log.error(e.getMessage());
        return Result.fail(BusinessEnum.OPERATION_FAIL.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtimeException(RuntimeException e) {
        log.error(e.getMessage());
        return Result.fail(BusinessEnum.SERVER_INNER_FAIL);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> accessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
//        return Result.fail(BusinessEnum.ACCESS_DENY_FAIL);
        throw e;        //捕获不到这个错误，需要往上继续抛出
    }
}
