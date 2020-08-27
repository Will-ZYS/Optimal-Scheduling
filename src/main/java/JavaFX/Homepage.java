package JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class Homepage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        Parent root = new Label("welcome");
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();

        Parent root = null;
        try {
            //root = FXMLLoader.load(getClass().getClassLoader().getResource("/Homepage.fxml"));
            root = FXMLLoader.load(getClass().getResource("/JavaFX/Homepage.fxml"));

        } catch (IOException e) {
            throw new RuntimeException();
        }

        //Parent root = new JFXButton("aaa");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();



    }



}

