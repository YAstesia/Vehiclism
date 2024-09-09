package com.seu.vehiclism;

public class Response<T>{
    private T data;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private boolean success;
    private String msg;

    public static <K>Response<K>newSuccess(K data,String msg){
        Response<K> response=new Response<>();
        response.setData(data);
        response.setMsg(msg);
        response.setSuccess(true);
        response.setCode(200);
        return response;
    }
    public static <K>Response<K>newFail(String errorMsg){
        Response<K>response=new Response<>();
        response.setMsg(errorMsg);
        response.setSuccess(false);
        response.setCode(-1);
        return response;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String Msg) {
        this.msg = Msg;
    }

}
