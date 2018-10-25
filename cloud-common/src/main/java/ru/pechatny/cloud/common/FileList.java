package ru.pechatny.cloud.common;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileList implements Serializable {
    private ArrayList<String> filesList = new ArrayList<>();

    public ArrayList<String> getFilesList() {
        return filesList;
    }

    public void setFilesList(ArrayList<String> filesList) {
        this.filesList = filesList;
    }

    public void add(Path item, String basePath) {
        String path = item.toString();
        if (Files.isDirectory(item)) {
            path += "/";
        }
        path = path.substring(basePath.length(), path.length());

        filesList.add(path);
    }

}
