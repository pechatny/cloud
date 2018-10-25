package ru.pechatny.cloud.common;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private byte[] data;
    private String path;

    public FileMessage(byte[] data, String path) {
        this.data = data;
        this.path = path;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
