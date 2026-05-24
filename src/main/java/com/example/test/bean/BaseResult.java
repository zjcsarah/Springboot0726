package com.example.test.bean;

/**
 * 统一响应结果封装
 */
public class BaseResult<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> BaseResult<T> ok(T data) {
        BaseResult<T> r = new BaseResult<>();
        r.code = 200;
        r.msg = "操作成功";
        r.data = data;
        return r;
    }

    public static <T> BaseResult<T> ok(T data, String msg) {
        BaseResult<T> r = new BaseResult<>();
        r.code = 200;
        r.msg = msg;
        r.data = data;
        return r;
    }

    public static <T> BaseResult<T> fail(String msg) {
        BaseResult<T> r = new BaseResult<>();
        r.code = -1;
        r.msg = msg;
        return r;
    }

    public static <T> BaseResult<T> fail(int code, String msg) {
        BaseResult<T> r = new BaseResult<>();
        r.code = code;
        r.msg = msg;
        return r;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}