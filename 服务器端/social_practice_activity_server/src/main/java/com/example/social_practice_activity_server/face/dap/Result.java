package com.example.social_practice_activity_server.face.dap;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: Result
 * @Description: TODO
 * @author:
 * @createDate:
 */
@ToString
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -6287952131441663819L;

    /**
     * 编码
     */
    private int code = 200;

    /**
     * 成功标志
     */
    private Boolean success;

    /**
     * 返回消息
     */
    private String msg = "操作成功";

    /**
     * 返回数据
     */
    private T data;

    /**
     * 创建实例
     *
     * @return
     */
    public static Result instance() {
        return new Result();
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Result setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public static Result ok() {
        return Result.instance().setSuccess(true);
    }

    public static Result ok(Object data) {
        return ok().setData(data);
    }

    public static Result ok(Object data, String msg) {
        return ok(data).setMsg(msg);
    }

    public static Result error() {
        return Result.instance().setSuccess(false);
    }

    public static Result error(String msg) {
        return error().setMsg(msg);
    }

    /**
     * 无参
     */
    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * 有全参
     *
     * @param code
     * @param msg
     * @param data
     * @param success
     */
    public Result(int code, String msg, T data, Boolean success) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = success;
    }

    /**
     * 有参
     *
     * @param code
     * @param msg
     * @param data
     */
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}