package ru.pechatny.cloud.server;

public class CloudState {
    private String basePath;

    public CloudState(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
