package ru.pechatny.cloud.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Properties;
import java.util.prefs.Preferences;

public class Main extends Application {

    public static Client client;
    public static Stage primaryStage;
    private Preferences preferences;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        preferences = Preferences.userRoot();
        boolean isAuthorized = preferences.getBoolean("authorized", false);
        readProperties();
        Main.primaryStage = primaryStage;

        String sceneSource = "mainUnauth.fxml";
//        String sceneSource = isAuthorized ? "mainWindow.fxml" : "mainUnauth.fxml";
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(sceneSource)));
        primaryStage.setTitle("Cloud Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void readProperties() {
        System.out.println(getClass().getClassLoader().getResource("config.properties").getPath());
        InputStream inputStream;
        Properties property = new Properties();

        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
            property.load(inputStream);

            String storagePath = property.getProperty("storage.path");
            System.out.println("path: " + storagePath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void writeProperties() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
            output = new FileOutputStream(getClass().getClassLoader().getResource("config.properties").getPath());


            // set the properties value
            prop.setProperty("database", "localhost");
            prop.setProperty("dbuser", "mkyong");
            prop.setProperty("dbpassword", "password");

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
