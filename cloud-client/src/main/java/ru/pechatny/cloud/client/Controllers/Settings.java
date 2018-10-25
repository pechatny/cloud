package ru.pechatny.cloud.client.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static ru.pechatny.cloud.client.Main.primaryStage;

public class Settings implements Initializable {
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    public TextField localPath;
    public Button saveButton;
    private Preferences preferences;

    public void saveSettings(ActionEvent actionEvent) {
        preferences.put("storagePath", localPath.getText());
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    public void chooseDirectory(ActionEvent actionEvent) {
        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            localPath.setText(dir.getAbsolutePath());
        } else {
            localPath.setText(null);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        preferences = Preferences.userRoot();
        localPath.setText(preferences.get("storagePath", ""));
    }
}
