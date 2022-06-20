package com.example.demo.model;

public class TitResult {

    //响应代码
    private Integer code;

    //响应消息
    private String msg;

    //响应数据
    private Object data;

    public TitResult() {
    }

    public TitResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static TitResult getTitResult(Integer code, String msg, Object data){
        return new TitResult(code, msg, data);
    }

    public static TitResult getTitResult(Integer code, String msg){
        return new TitResult(code, msg, null);
    }

    public static TitResult success(Object data){
        return new TitResult(200, "", data);
    }

    //…………getter setter…………
}
