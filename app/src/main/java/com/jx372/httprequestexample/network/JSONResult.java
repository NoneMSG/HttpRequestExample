package com.jx372.httprequestexample.network;


import java.util.List;

/**
 * Created by bit-user on 2017-07-28.
 */
//제네릭 타입 클래스 만들어준다 이 클래스를 상속해서 사용하면 된다.
public class JSONResult<T> {
    private String result ;
    private String message ;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
