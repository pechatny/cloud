package ru.pechatny.cloud.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.pechatny.cloud.common.LoginRequest;
import ru.pechatny.cloud.common.SuccessResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Main extends Application {

    public static Stage primaryStage;
    private Preferences preferences;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        preferences = Preferences.userRoot();
        Main.primaryStage = primaryStage;

        String sceneSource;
        if (isAuthorized() && authorize()) {
            sceneSource = "mainWindow.fxml";
        } else {
            sceneSource = "mainUnauth.fxml";
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(sceneSource)));
        root.getStylesheets().add(getClass().getClassLoader().getResource("FlatBee.css").toString());
        primaryStage.setTitle("Cloud Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private boolean authorize() {
        try {
            Client client = Client.getInstance();
            String login = preferences.get("login", "");
            String password = preferences.get("password", "");

            LoginRequest loginRequest = new LoginRequest(login, password);
            SuccessResponse response = client.login(loginRequest);

            return response.isSuccess();
        } catch (IOException e) {
            WindowManager.showErrorAlert("Ошибка соединения с сервером", e.getMessage());
            WindowManager.changeMainStage("mainUnauth.fxml");
        }

        return false;
    }

    private boolean isAuthorized() {
        String login = preferences.get("login", null);
        String password = preferences.get("password", null);

        return (login != null && password != null);
    }
}
