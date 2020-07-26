package com.tengbin.blog.common.exception;

/**
 * 服务（业务）异常如"账号或密码错误 "
 * 该异常只做INFO级别的日志记录
 *
 * @author TengBin
 */
public class ServiceException extends RuntimeException {

    /**
     * 1.无参构造
     */
    public ServiceException() {

    }

    /**
     * 2.有参构造(只返回错误信息)
     *
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * 3.有参构造(返回错误信息及造成原因)
     *
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
