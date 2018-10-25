package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.pechatny.cloud.client.Client;
import ru.pechatny.cloud.client.Main;
import ru.pechatny.cloud.common.RegistrationRequest;
import ru.pechatny.cloud.common.SuccessResponse;

public class RegistrationController {
    public TextField loginField;
    public PasswordField passwordField;
    public Button cancelButton;
    public PasswordField passwordConfirmation;

    public void cancelAction(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void registerAction(ActionEvent actionEvent) {
        if (Main.client == null) {
            Main.client = new Client("localhost", 8189);
            Main.client.run();
        }

        String login = loginField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmation.getText();

        if (!password.equals(passwordConfirm)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Ошибка");
            alert.setHeaderText("Ошибка при регистрации!");
            alert.setContentText("Пароли должны совпадать, попробуйте ещё раз.");

            alert.showAndWait();
        }

        RegistrationRequest request = new RegistrationRequest(login, password);
        SuccessResponse response = Main.client.registration(request);
        if (response.isSuccess()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Успешно");
            alert.setHeaderText("Регистрация прошла успешно!");

            alert.showAndWait();
        }
    }
}
