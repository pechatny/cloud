package ru.pechatny.cloud.client;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;

public class FileRemoteItem extends TreeItem {
    private String fullPath;
    private String shortPath;
    private Boolean isDirectory;

    public FileRemoteItem(String value, Client client) {
        super(value);

        this.fullPath = value;

        if (!fullPath.endsWith(File.separator)) {
            isDirectory = false;
            //set the value (which is what is displayed in the tree)
            String treeValue = value;
            int indexOf = value.lastIndexOf(File.separator);
            if (indexOf > 0) {
                this.setValue(value.substring(indexOf + 1));
            } else {
                this.setValue(value);
            }
        } else {
            isDirectory = true;
        }

        this.addEventHandler(TreeItem.branchExpandedEvent(), e -> {
            FileRemoteItem source = (FileRemoteItem) e.getSource();
//                if(source.isDirectory()&&source.isExpanded()){
//                    ImageView iv=(ImageView)source.getGraphic();
//                    iv.setImage(folderExpandImage);
//                }
            ArrayList<String> fileList = client.getFileList(source.getFullPath()).getFilesList();
            if (source.getChildren().size() < fileList.size()) {
                source.getChildren().removeAll(source.getChildren());
//                Path path = Paths.get(source.getFullPath());
                for (String file : fileList) {
                    FileRemoteItem treeNode = new FileRemoteItem(file, client);
                    source.getChildren().add(treeNode);
                }
            } else {
                //refresh
            }
        });
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getShortPath() {
        return shortPath;
    }

    public void setShortPath(String shortPath) {
        this.shortPath = shortPath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public boolean isLeaf() {
        return !isDirectory;
    }
}
