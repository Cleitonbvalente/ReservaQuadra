package com.reservaquadraoficial.reservaquadrateste;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Sistema de Reservas do Ginásio");
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage criarTela(String url) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource(url));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        return stage;
    }

    public static void main(String[] args) {
        launch();
    }
}
