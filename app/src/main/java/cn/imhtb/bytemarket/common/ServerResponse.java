package cn.imhtb.bytemarket.common;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public boolean isSuccess(){
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
