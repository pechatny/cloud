package ru.pechatny.cloud.common;

import java.io.Serializable;

public class SuccessResponse implements Serializable {
    private boolean success;

    public SuccessResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
