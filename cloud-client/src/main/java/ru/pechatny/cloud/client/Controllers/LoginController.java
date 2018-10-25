package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.pechatny.cloud.client.Client;
import ru.pechatny.cloud.client.Main;
import ru.pechatny.cloud.common.LoginRequest;
import ru.pechatny.cloud.common.SuccessResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

public class LoginController {
    public TextField loginField;
    public PasswordField passwordField;
    public Button loginButton;
    public Button cancelButton;

    public void loginAction(ActionEvent actionEvent) {
        if (Main.client == null) {
            Main.client = new Client("localhost", 8189);
            Main.client.run();
        }


        String login = loginField.getText();
        String password = passwordField.getText();

        LoginRequest loginRequest = new LoginRequest(login, password);
        SuccessResponse response = Main.client.login(loginRequest);

        if (response.isSuccess()) {
            loginButton.setText("Success!");
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("mainWindow.fxml")));
                Scene scene = new Scene(root);
                Main.primaryStage.setScene(scene);
                Preferences.userRoot().putBoolean("authorized", true);
                ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверный логин или пароль!");

            alert.showAndWait();
        }
    }

    public void cancelAction(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }
}
