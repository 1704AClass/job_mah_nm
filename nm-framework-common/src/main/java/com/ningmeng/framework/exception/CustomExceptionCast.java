package com.ningmeng.framework.exception;

import com.ningmeng.framework.model.response.ResultCode;

public class CustomExceptionCast {
    //使用静态方法跑出自定义异常
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
