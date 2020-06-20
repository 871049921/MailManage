package jn_17201312.Controller;

import jn_17201312.Client.ClientUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import jn_17201312.model.IODeal;
import jn_17201312.model.Mail;
import jn_17201312.model.MailSenderUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceReadLetterController implements Initializable {

    public Button btSendToExtranet;
    ClientUI application;
    private String address;
    private String fileName;

    @FXML
    public Label labWelcome;
    @FXML
    public TextArea taContext;
    @FXML
    public Label lbSender;
    @FXML
    public Label lbSubject;
    @FXML
    public Label lbTime;
    @FXML
    public Button btBack;
    @FXML
    public Button btLogout;

    public Mail mail;


    public void setApp(ClientUI application, String address, String fileName) {
        this.application = application;
        labWelcome.setText("欢迎：管理员");
        this.address = address;
        this.fileName = fileName;
        String path = "Mails/" + address + "/" + fileName;
        loadAMail(path);
    }

    public void loadAMail(String path) {
        JSONObject jsonObject = IODeal.read(path);
        mail = IODeal.toMail(jsonObject);
        lbSender.setText(mail.getSender());
        lbSubject.setText(mail.getSubject());
        lbTime.setText(mail.getTime());
        taContext.setText(mail.getContext());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onClickBack() {
        application.goServiceMain(address);
    }

    @FXML
    public void onClickLogout() {
        application.goLogin();
    }

    @FXML
    public void onClickSendToExtranet() {
        new Thread(() -> {
            MailSenderUtils mailSenderUtils = new MailSenderUtils("smtp", "smtp", "smtp.163.com");
            mailSenderUtils.setMail(mail);
            mailSenderUtils.setRecipientAddress("hwd871049921@163.com");
            mailSenderUtils.setSenderAddress("hwd871049921@163.com");
            mailSenderUtils.setSenderAccount("hwd871049921@163.com");
            mailSenderUtils.setSenderPassword("KYJUJEFVADVTYQSG");
            //mailSenderUtils.setSessionDebug(true);
            boolean flag = mailSenderUtils.send();
            if (flag) {
                Platform.runLater(() -> {
                    application.alertSendMessage("发送成功");
                });
            } else {
                Platform.runLater(() -> {
                    application.alertSendMessage("发送失败");
                });
            }
        }).start();
    }
}
