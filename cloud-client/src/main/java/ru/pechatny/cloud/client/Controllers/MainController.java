package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.pechatny.cloud.client.Client;
import ru.pechatny.cloud.client.FileItem;
import ru.pechatny.cloud.client.FileRemoteItem;
import ru.pechatny.cloud.client.WindowManager;
import ru.pechatny.cloud.common.FileList;
import ru.pechatny.cloud.common.FileMessage;
import ru.pechatny.cloud.common.RequestMessage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static ru.pechatny.cloud.client.Main.primaryStage;

public class MainController implements Initializable {

    public TextField textField;
    public Button exitButton;
    private String basePath = "/Users/d.pechatnikov/study/storage/";
    private Preferences preferences;

    @FXML
    private TreeView<String> serverTree;

    @FXML
    private TreeView<String> mainTree;
    private Callback<TreeView<String>, TreeCell<String>> defaultCellFactory = new Callback<TreeView<String>, TreeCell<String>>() {
        @Override
        public TreeCell<String> call(TreeView<String> stringTreeView) {
            TreeCell<String> treeCell = new TreeCell<String>() {
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            return treeCell;
        }
    };

    private TreeItem<String> getRootNode() {
        preferences = Preferences.userRoot();
        String basePath = preferences.get("storagePath", System.getProperty("user.home"));

        TreeItem<String> rootNode = new TreeItem<>(basePath);
        Path baseDirPaths = Paths.get(basePath);

        try {
            DirectoryStream<Path> dir = Files.newDirectoryStream(baseDirPaths);
            for (Path file : dir) {
                FileItem node = new FileItem(file);
                rootNode.getChildren().add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootNode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Client client = Client.getInstance();

            TreeItem<String> rootNode = getRootNode();

            String remoteServerBasePath = "/";
            FileList list = client.getFileList(remoteServerBasePath);

            FileRemoteItem serverNode = new FileRemoteItem(remoteServerBasePath, client);

            if (serverNode.getChildren().isEmpty()) {
                for (String listItem : list.getFilesList()) {
                    FileRemoteItem node = new FileRemoteItem(listItem, client);
                    serverNode.getChildren().add(node);
                }
            }

            mainTree.setRoot(rootNode);
            serverTree.setRoot(serverNode);

            ContextMenu menu = new ContextMenu(new MenuItem("Delete"));
            menu.setOnAction(event -> {
                FileRemoteItem item = (FileRemoteItem) serverTree.getSelectionModel().getSelectedItem();
                boolean deleteResult;
                if (item.isDirectory()) {
                    deleteResult = client.deleteDirectory(item.getFullPath());
                } else {
                    deleteResult = client.deleteFile(item.getFullPath());
                }

                if (item != serverTree.getRoot() && deleteResult) {
                    ((FileRemoteItem) serverTree.getSelectionModel()
                            .getSelectedItem())
                            .getParent()
                            .getChildren()
                            .remove(item);
                }

            });

            mainTree.setOnDragDetected(event -> {
                FileItem fileItem = (FileItem) mainTree.getSelectionModel().getSelectedItem();
                System.out.println(" Detected ");
                Dragboard db = mainTree.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(fileItem.getFullPath());
                db.setContent(content);
                event.consume();
            });

            serverTree.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            });

            serverTree.setOnDragDropped(event -> {
                event.acceptTransferModes(TransferMode.ANY);
                Dragboard dragboard = event.getDragboard();
                String filePath = dragboard.getString();
                System.out.println("Drop " + filePath);
                System.out.println("Drop done detected");

                sendFile(filePath, client);

                serverTree.getRoot().getChildren().removeAll(serverTree.getRoot().getChildren());
                serverNode.setExpanded(false);
                serverNode.setExpanded(true);
                event.consume();
            });

            mainTree.setCellFactory(defaultCellFactory);
            serverTree.setCellFactory(defaultCellFactory);
            serverTree.setContextMenu(menu);

            rootNode.setExpanded(true);
            serverNode.setExpanded(true);
        } catch (IOException e) {
            WindowManager.showErrorAlert("Ошибка соединения с сервером", e.getMessage());
            WindowManager.changeMainStage("mainUnauth.fxml");
        }
    }

    private void sendFile(String filePath, Client client) {

        Path filePathItem = Paths.get(filePath);

        if (Files.isDirectory(filePathItem)) {
            try {
                Files.newDirectoryStream(filePathItem).forEach(item -> {
                    sendFile(item.toString(), client);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Path relativePath = getRelativePath(filePath);
            System.out.println(relativePath);
            try {
                FileMessage sendFile = new FileMessage(Files.readAllBytes(filePathItem), relativePath.toString());
                RequestMessage response = (RequestMessage) client.sendFile(sendFile);
                System.out.println("Response: " + response.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Path getRelativePath(String path) {
        Path basePathItem = Paths.get(basePath);
        Path filePathItem = Paths.get(path);

        return Paths.get(path).subpath(basePathItem.getNameCount(), filePathItem.getNameCount());
    }

    public void openSettings(ActionEvent actionEvent) {
        Stage settings = WindowManager.getModalWindow("settings.fxml", primaryStage);
        settings.show();
        settings.setOnHidden(event -> {
            TreeItem<String> rootNode = getRootNode();
            mainTree.setRoot(rootNode);
        });
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void logout(ActionEvent actionEvent) {
        try {
            Client.getInstance().disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WindowManager.changeMainStage("mainUnauth.fxml");
    }
}
