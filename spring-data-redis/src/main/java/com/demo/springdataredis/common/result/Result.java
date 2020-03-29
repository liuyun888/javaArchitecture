package com.demo.springdataredis.common.result;


/**
 * @author Administrator
 */
public class Result {

  private Integer code;

  private String msg;

  private Object data;

  public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(1000, "成功"),
    /**
     * 失败
     */
    FAIL(1001, "失败"),
    /**
     * 错误
     */
    ERROR(1002, "错误"),
    /**
     * 异常
     */
    EXCEPTION(1003, "异常");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
      this.code = code;
      this.message = message;
    }

    public Integer code() {
      return this.code;
    }

    public String message() {
      return this.message;
    }

    public static String getMessage(String name) {
      for (ResultCode item : ResultCode.values()) {
        if (item.name().equals(name)) {
          return item.message;
        }
      }
      return name;
    }

    public static Integer getCode(String name) {
      for (ResultCode item : ResultCode.values()) {
        if (item.name().equals(name)) {
          return item.code;
        }
      }
      return null;
    }

    @Override
    public String toString() {
      return this.name();
    }
  }

  public Result() {
  }

  public Result(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static Result success() {
    Result result = new Result();
    result.setResultCode(ResultCode.SUCCESS);
    return result;
  }

  public static Result success(String msg) {
    Result result = new Result();
    result.setResultCode(ResultCode.SUCCESS);
    result.setMsg(msg);
    return result;
  }

  public static Result success(Object data) {
    Result result = new Result();
    result.setResultCode(ResultCode.SUCCESS);
    result.setData(data);
    return result;
  }

  public static Result success(String msg, Object data) {
    Result result = new Result();
    result.setResultCode(ResultCode.SUCCESS);
    result.setMsg(msg);
    result.setData(data);
    return result;
  }

  public static Result failure() {
    Result result = new Result();
    result.setResultCode(ResultCode.FAIL);
    return result;
  }

  public static Result failure(String msg) {
    Result result = new Result();
    result.setResultCode(ResultCode.FAIL);
    result.setMsg(msg);
    return result;
  }

  public static Result failure(ResultCode resultCode) {
    Result result = new Result();
    result.setResultCode(resultCode);
    return result;
  }

  public static Result failure(ResultCode resultCode, Object data) {
    Result result = new Result();
    result.setResultCode(resultCode);
    result.setData(data);
    return result;
  }

  public static Result exception() {
    Result result = new Result();
    result.setResultCode(ResultCode.EXCEPTION);
    return result;
  }

  public static Result exception(String msg) {
    Result result = new Result();
    result.code = 1002;
    result.msg = msg;
    return result;
  }

  public static Result exception(Integer code, String msg) {
    Result result = new Result();
    result.code = code;
    result.msg = msg;
    return result;
  }

  private void setResultCode(ResultCode code) {
    this.code = code.code();
    this.msg = code.message();
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
