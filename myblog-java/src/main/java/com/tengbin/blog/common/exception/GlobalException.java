package com.tengbin.blog.common.exception;

import com.tengbin.blog.common.utils.Result;
import com.tengbin.blog.common.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName:GlobalException
 * @Description: 全局异常处理
 * @Author Mr.T
 * @date 2020/6/22 0:43
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * 未认证异常
     *
     * @param shiroException
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException shiroException) {
        log.error("未认证异常：----------------: ", shiroException);
        return ResultGenerator.genFailResult(shiroException.getMessage());
    }


    /**
     * 实体校验异常
     *
     * @param methodArgumentNotValidException
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("实体校验异常：----------------: ", methodArgumentNotValidException);
        return ResultGenerator.genFailResult(methodArgumentNotValidException.getMessage());
    }

    /**
     * 非法操作异常
     *
     * @param illegalArgumentException
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException illegalArgumentException) {
        log.error("Assert异常：----------------: ", illegalArgumentException);
        return ResultGenerator.genFailResult(illegalArgumentException.getMessage());
    }

    /**
     * 运行时异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException runtimeException) {
        log.error("运行时异常：----------------: ", runtimeException);
        return ResultGenerator.genFailResult(runtimeException.getMessage());
    }

}
