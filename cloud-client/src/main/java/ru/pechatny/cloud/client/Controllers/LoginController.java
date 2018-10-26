package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.pechatny.cloud.client.Client;
import ru.pechatny.cloud.client.WindowManager;
import ru.pechatny.cloud.common.LoginRequest;
import ru.pechatny.cloud.common.SuccessResponse;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {
    public TextField loginField;
    public PasswordField passwordField;
    public Button loginButton;
    public Button cancelButton;
    public CheckBox saveCheckbox;

    public void loginAction(ActionEvent actionEvent) {
        Client client = Client.getInstance();

        String login = loginField.getText();
        String password = passwordField.getText();

        LoginRequest loginRequest = new LoginRequest(login, password);
        SuccessResponse response = client.login(loginRequest);

        if (response.isSuccess()) {
            loginButton.setText("Success!");
            WindowManager.changeMainStage("mainWindow.fxml");
            if (saveCheckbox.isSelected()) {
                Preferences.userRoot().put("login", login);
                Preferences.userRoot().put("password", password);
                Preferences.userRoot().putBoolean("credentialsSaved", true);
            } else {
                Preferences.userRoot().remove("login");
                Preferences.userRoot().remove("password");
                Preferences.userRoot().putBoolean("credentialsSaved", false);
            }

            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String login = Preferences.userRoot().get("login", null);
        String password = Preferences.userRoot().get("password", null);
        boolean saved = Preferences.userRoot().getBoolean("credentialsSaved", false);
        if (login != null && password != null) {
            loginField.setText(login);
            passwordField.setText(password);
        }

        saveCheckbox.setSelected(saved);
    }
}
