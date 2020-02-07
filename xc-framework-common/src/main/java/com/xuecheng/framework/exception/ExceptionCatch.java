package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常捕获类
 * @Author: Pace
 * @Data: 2020/2/7 15:08
 * @Version: v1.0
 */
@ControllerAdvice // 控制器增强
@Slf4j
public class ExceptionCatch {

    // ImmutableMap是谷歌封装的可读不可写的Map，线程安全
    // 我们在这里用来存储不可预知的异常类型
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    // ImmutableMap的构造器
    protected static ImmutableMap.Builder <Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }

    // 捕获自定义异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 需要json返回
    public ResponseResult customException(CustomException e){
        log.error("catch customException ：{}",e.getResultCode());
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    // 捕获未知异常， Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        log.error("catch Exception ：{} \r\n exception: ",e.getMessage(),e);
        // 首先判断，未知异常是否存放在Map中
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if(resultCode != null){ // 说明存在，返回定义的错误信息
            return new ResponseResult(resultCode);
        }else {
            // 不存在，返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
}
