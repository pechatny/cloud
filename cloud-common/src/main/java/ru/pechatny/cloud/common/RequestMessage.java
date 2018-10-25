package ru.pechatny.cloud.common;

import java.io.Serializable;

public class RequestMessage implements Serializable {
    private String data;

    public RequestMessage(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
