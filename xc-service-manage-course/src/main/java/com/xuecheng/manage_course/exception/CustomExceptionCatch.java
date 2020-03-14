package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;


/**
 * @Author: Pace
 * @Data: 2020/3/14 16:19
 * @Version: v1.0
 */
@ControllerAdvice // 控制器增强
@Slf4j
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        builder.put(AccessDeniedException.class,CommonCode.UNAUTHORISE);
    }

}
