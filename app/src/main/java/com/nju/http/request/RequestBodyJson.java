package com.nju.http.request;

/**
 * Created by xiaojuzhang on 2016/3/24.
 */
public class RequestBodyJson<T> {

    private String authorization;
    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }


}
