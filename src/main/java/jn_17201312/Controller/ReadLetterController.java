package jn_17201312.Controller;

import jn_17201312.Client.Client;
import jn_17201312.Client.ClientUI;
import com.alibaba.fastjson.JSONObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import jn_17201312.model.IODeal;
import jn_17201312.model.Mail;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ReadLetterController implements Initializable {

    @FXML
    public Button btWriteLetter;
    @FXML
    public Button btReceiveLetter;
    @FXML
    public Label labWelcome;
    @FXML
    public TextArea taContext;
    @FXML
    public Button btLogout;
    @FXML
    public Label lbSender;
    @FXML
    public Label lbSubject;
    @FXML
    public Label lbTime;

    private ClientUI application;
    private String address;
    private String fileName;
    private final Client client = new Client();
    private Socket socket;

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

    public void setApp(ClientUI application, String address, String fileName, Socket socket) {
        this.application = application;
        labWelcome.setText("欢迎：" + address);
        this.address = address;
        this.fileName = fileName;
        this.socket = socket;
        System.out.println("ReadSocket:" + socket);
        String path = "Users/" + address + "/" + fileName;
        loadMails(path);
    }

    public void loadMails(String path) {
        JSONObject jsonObject = IODeal.read(path);
        Mail mail = IODeal.toMail(jsonObject);
        lbSender.setText(mail.getSender());
        lbSubject.setText(mail.getSubject());
        lbTime.setText(mail.getTime());
        taContext.setText(mail.getContext());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
