package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.pechatny.cloud.client.WindowManager;

import static ru.pechatny.cloud.client.Main.primaryStage;

public class UnauthorizedController {

    public Button loginAction;
    public Button registrationButton;

    public void openLoginForm(ActionEvent actionEvent) {
        Stage loginStage = WindowManager.getModalWindow("loginForm.fxml", primaryStage);
        loginStage.show();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void openRegistrationForm(ActionEvent actionEvent) {
        Stage registration = WindowManager.getModalWindow("registrationForm.fxml", primaryStage);
        registration.show();
    }
}
