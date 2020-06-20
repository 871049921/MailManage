package jn_17201312.Controller;

import jn_17201312.Client.ClientUI;
import jn_17201312.Client.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    private ClientUI application;
    private String address;
    private final Client client = new Client();
    private Socket socket;

    @FXML
    public Label labWelcome;
    @FXML
    public Button btLogout;
    @FXML
    public TextArea taContext;
    @FXML
    public Button btSend;
    @FXML
    public TextField tfReceiver;
    @FXML
    public TextField tfSubject;
    @FXML
    public Button btWriteLetter;
    @FXML
    public Button btReceiveLetter;

    @FXML
    public void onClickSendLetter() {
        if (canSendMail()) {
            new Thread(() -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.sendLetter(address, tfReceiver.getText(), tfSubject.getText(), taContext.getText(), application);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setApp(ClientUI application, String address, Socket socket) {
        this.application = application;
        labWelcome.setText("欢迎：" + address);
        this.address = address;
        this.socket = socket;
        System.out.println("MainSocket:" + socket);
    }

    public boolean canSendMail() {
        String receiver = tfReceiver.getText();
        String subject = tfSubject.getText();
        String context = taContext.getText();
        String regex = "^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tfReceiver.getText());

        if (null == receiver || "".equals(receiver)) {
            application.alertSendMessage("发送失败！收信人未填写");
            return false;
        }
        if (!m.matches()) {
            application.alertSendMessage("发送失败！收信人邮箱格式不正确");
            return false;
        }
        if (null == subject || "".equals(subject)) {
            application.alertSendMessage("发送失败！主题未填写");
            return false;
        }
        if (null == context || "".equals(context)) {
            application.alertSendMessage("发送失败！正文未填写");
            return false;
        }
        return true;
    }
}
