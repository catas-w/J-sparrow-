package com.catas.audit.controller;

import com.catas.audit.common.ResultObj;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.StringJoiner;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    public ResultObj defaultAuthorizedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return ResultObj.NO_PERMISSION;
    }

    // @ExceptionHandler(UnauthenticatedException.class)
    // public String defaultAuthenticatedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
    //     return defaultException(request, response);
    // }



    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultObj handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        // 按需重新封装需要返回的错误信息 解析原错误信息，
        // 封装后返回，此处返回非法的字段名称error.getField()，
        // 原始值error.getRejectedValue()，错误信息
        StringJoiner sj = new StringJoiner(";");
        // e.getBindingResultObj().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        e.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        return ResultObj.error(sj.toString());
    }


    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ResultObj handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return ResultObj.error(e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultObj handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        StringJoiner sj = new StringJoiner(";");
        e.getConstraintViolations().forEach(x -> sj.add(x.getMessageTemplate()));
        return ResultObj.error(sj.toString());
    }


}
