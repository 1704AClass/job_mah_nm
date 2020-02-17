package com.ningmeng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.framework.model.response.ResultCode;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 控制器增强类 类似于aop通知
 */
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);
    //ImmutableMap 一旦定义 不可修改
    ImmutableMap<Class<? extends Throwable>, ResultCode> immutableMap;

    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class,CommonCode.HTTP_ERROR);
    }

    //异常控制器
    @ExceptionHandler
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch customException : {}\r\nexception: ",e.getMessage(), e);
        return new ResponseResult(e.getResultCode());
    }

    @ExceptionHandler
    @ResponseBody
    public ResponseResult Exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(), e);
        if(immutableMap == null){
            //初始化
            immutableMap = builder.build();
        }
        ResultCode resultCode = immutableMap.get(e.getClass());
        if (resultCode != null){
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }
}
