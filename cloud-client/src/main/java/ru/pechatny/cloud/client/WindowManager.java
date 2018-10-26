package ru.pechatny.cloud.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
}
