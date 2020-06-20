package jn_17201312.Controller;

import jn_17201312.Client.Client;
import jn_17201312.Client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jn_17201312.model.IODeal;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceiveLetterController implements Initializable {

    @FXML
    public AnchorPane apMain;
    @FXML
    public Button btWriteLetter;
    @FXML
    public Button btReceiveLetter;
    @FXML
    public Label labWelcome;
    @FXML
    public VBox vbList;
    @FXML
    public Button btLogout;

    private ClientUI application;

    private String address;
    private Socket socket;
    private final Client client = new Client();

    public void setApp(ClientUI application, String address, Socket socket) {
        this.application = application;
        labWelcome.setText("欢迎：" + address);
        this.address = address;
        this.socket = socket;
        System.out.println("SendSocket:" + socket);
        // 从服务器获取邮件
        new Thread(() -> {
            try {
                client.loadMailFromService(address);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loadMails();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void onClickLogout() {
        try {
            if (client.logout(socket)) {
                application.alertSendMessage("退出成功！");
                application.goLogin();
            } else {
                application.alertSendMessage("退出失败！");
            }
        } catch (IOException e) {
            application.alertSendMessage("退出失败！");
        }
    }

    @FXML
    public void onClickWriteLetter() {
        application.goMain(address, socket);
    }

    @FXML
    public void onClickReceiveLetter() {
        application.goReceiveLetter(address, socket);
    }

    public void loadMails() {
        File[] files = IODeal.getFilesFromFolder("Users/" + address, ".json");
        for (final File file : files) {
            Hyperlink tempLink = new Hyperlink(file.getName());
            tempLink.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    application.goReadLetter(address, file.getName(), socket);
                }
            });
            vbList.getChildren().add(tempLink);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
