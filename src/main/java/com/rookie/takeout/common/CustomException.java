package com.rookie.takeout.common;

/**
 * @title: CustomException
 * @Author: Mrdong
 * @Date: 2022/8/28 19:28
 * @Description:自定义业务异常类
 */
public class CustomException extends RuntimeException{

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
}
