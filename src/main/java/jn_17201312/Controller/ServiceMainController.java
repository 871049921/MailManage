package jn_17201312.Controller;

import jn_17201312.Client.ClientUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import jn_17201312.model.IODeal;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ServiceMainController implements Initializable {
    @FXML
    public VBox vbList;
    @FXML
    public Label labWelcome;
    @FXML
    public Button btBack;
    @FXML
    public Button btLogout;
    @FXML
    public Label labAliveNumber;

    private ClientUI application;
    private String address;

    public void loadUsers() {
        btBack.setVisible(false);
        vbList.getChildren().clear();
        File[] files = IODeal.getFilesFromFolder("Mails/", "");
        System.out.println(files.length);
        for (final File file : files) {
            Hyperlink tempLink = new Hyperlink(file.getName());
            tempLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    address = tempLink.getText();
                    vbList.getChildren().clear();
                    loadAllMails();
                }
            });
            vbList.getChildren().add(tempLink);
        }
    }

    public void setApp(ClientUI application) {
        this.application = application;
        labWelcome.setText("欢迎：管理员");
    }

    public void setApp(ClientUI application, String address) {
        this.application = application;
        labWelcome.setText("欢迎：管理员");
        this.address = address;
        loadAllMails();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUsers();
    }

    public void loadAllMails() {
        btBack.setVisible(true);
        File[] files = IODeal.getFilesFromFolder("Mails/" + address, ".json");
        for (final File file : files) {
            Hyperlink tempLink = new Hyperlink(file.getName());
            tempLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    application.goServiceReadLetter(address, file.getName());
                }
            });
            vbList.getChildren().add(tempLink);
        }
    }

    @FXML
    public void onClickBack() {
        loadUsers();
    }

    @FXML
    public void onClickLogout() {
        application.goLogin();
    }
}
