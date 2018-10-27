package ru.pechatny.cloud.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowManager {

    public static Stage getModalWindow(String fxml, Stage owner) {
        final Stage loginStage = new Stage();
        loginStage.initModality(Modality.WINDOW_MODAL);
        loginStage.initOwner(owner);
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(WindowManager.class.getClassLoader().getResource(fxml)));
            root.getStylesheets().add(WindowManager.class.getClassLoader().getResource("FlatBee.css").toString());

            Scene scene = new Scene(root);
            loginStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loginStage;
    }

    public static void changeMainStage(String sceneSource) {
        Stage primaryStage = Main.primaryStage;
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(WindowManager.class.getClassLoader().getResource(sceneSource)));
            root.getStylesheets().add(WindowManager.class.getClassLoader().getResource("FlatBee.css").toString());
            primaryStage.setTitle("Cloud Manager");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showErrorAlert(String title, String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait();
    }
}
