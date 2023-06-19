package com.ustlearn.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
//aop的封装类,@ControllerAdvice本质上是一个component,是为那些声明了（@ExceptionHandler、@InitBinder 或 @ModelAttribute注解修饰的）
// 方法的类而提供的专业化的@Component,这里采用注解的方式指定要拦截的类
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//返回json数据加注解
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Result.error(msg);
        }
        return Result.error("未知错误");
    }

    /**
     * 异常处理方法
     *用在顾客删除
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex) {
        log.info(ex.getMessage());

        return Result.error(ex.getMessage());
    }

}
