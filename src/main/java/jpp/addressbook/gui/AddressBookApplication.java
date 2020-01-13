package jpp.addressbook.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class AddressBookApplication extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    public void mainWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(AddressBookApplication.class.getResource("MainWindow.fxml"));
            AnchorPane pane  = loader.load();

            primaryStage.setMinHeight(800.00);
            primaryStage.setMinWidth(700.00);
            primaryStage.setMaxHeight(800.00);
            primaryStage.setMaxWidth(700.00);

            MainWindowController mainWindowController = loader.getController();
            mainWindowController.setMain(this);

            Scene scene = new Scene(pane);

            primaryStage.setScene(scene);
            primaryStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
