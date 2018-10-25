package ru.pechatny.cloud.client;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class FileItem extends TreeItem {
    private String fullPath;
    private String shortPath;
    private Boolean isDirectory;

    public FileItem(Path value) {
        super(value);
        if (Files.isDirectory(value)) {
            isDirectory = true;
//            this.setGraphic(new ImageView(folderCollapseImage));
        } else {
            this.isDirectory = false;
//            this.setGraphic(new ImageView(fileImage));
        }

        this.fullPath = value.toString();

        if (!fullPath.endsWith(File.separator)) {
            //set the value (which is what is displayed in the tree)
            String treeValue = value.toString();
            int indexOf = treeValue.lastIndexOf(File.separator);
            if (indexOf > 0) {
                this.setValue(treeValue.substring(indexOf + 1));
            } else {
                this.setValue(treeValue);
            }
        }

        this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
            @Override
            public void handle(Event e) {
                FileItem source = (FileItem) e.getSource();
//                if(source.isDirectory()&&source.isExpanded()){
//                    ImageView iv=(ImageView)source.getGraphic();
//                    iv.setImage(folderExpandImage);
//                }
                try {
                    if (source.getChildren().isEmpty()) {
                        Path path = Paths.get(source.getFullPath());
                        BasicFileAttributes attribs = Files.readAttributes(path, BasicFileAttributes.class);
                        if (attribs.isDirectory()) {
                            DirectoryStream<Path> dir = Files.newDirectoryStream(path);
                            for (Path file : dir) {
                                FileItem treeNode = new FileItem(file);
                                source.getChildren().add(treeNode);
                            }
                            setExpanded(true);
                        }
                    } else {
                        //if you want to implement rescanning a directory for changes this would be the place to do it
                    }
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        });

        this.addEventHandler(TreeItem.branchCollapsedEvent(), new EventHandler() {
            @Override
            public void handle(Event e) {
                FileItem source = (FileItem) e.getSource();
                if (source.isDirectory() && !source.isExpanded()) {
//                    ImageView iv=(ImageView)source.getGraphic();
//                    iv.setImage(folderCollapseImage);
                }
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
