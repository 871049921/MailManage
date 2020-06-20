package jn_17201312.Client;

import jn_17201312.Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientUI extends Application {

    private Stage stage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        goLogin();
        stage.show();
    }

    public void alertSendMessage(String info) {
        new Alert(Alert.AlertType.NONE, info, ButtonType.CLOSE).show();
    }

    public void goLogin() {
        try {
            LoginController controller = (LoginController) replaceSceneContent("/Login.fxml");
            controller.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goMain(String address, Socket socket) {
        try {
            MainController controller = (MainController) replaceSceneContent("/Client.fxml");
            controller.setApp(this, address, socket);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goReceiveLetter(String address, Socket socket) {
        try {
            ReceiveLetterController controller = (ReceiveLetterController) replaceSceneContent("/ReceiveLetter.fxml");
            controller.setApp(this, address, socket);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goReadLetter(String address, String fileName, Socket socket) {
        try {
            ReadLetterController controller = (ReadLetterController) replaceSceneContent("/ReadLetter.fxml");
            controller.setApp(this, address, fileName, socket);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goServiceMain() {
        try {
            ServiceMainController controller = (ServiceMainController) replaceSceneContent("/ServiceMain.fxml");
            controller.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goServiceMain(String address) {
        try {
            ServiceMainController controller = (ServiceMainController) replaceSceneContent("/ServiceMain.fxml");
            controller.setApp(this, address);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goServiceReadLetter(String address, String fileName) {
        try {
            ServiceReadLetterController controller = (ServiceReadLetterController) replaceSceneContent("/ServiceReadLetter.fxml");
            controller.setApp(this, address, fileName);
        } catch (Exception ex) {
            Logger.getLogger(ClientUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = ClientUI.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(ClientUI.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 793, 570);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
}
