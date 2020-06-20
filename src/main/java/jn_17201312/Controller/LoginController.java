package jn_17201312.Controller;

import jn_17201312.Client.Client;
import jn_17201312.Client.ClientUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController implements Initializable {

    private ClientUI application;
    private Client client = new Client();

    @FXML
    public Button btLogin;
    @FXML
    public TextField tfMail;
    @FXML
    public TextField tfPassword;
    @FXML
    public TextField tfNotice;

    @FXML
    public void onClickLogin() {
        if (tfMail.getText().equals("admin")) {
            application.goServiceMain();
        } else if (!isValidAddress()) {
            tfNotice.setText("登录失败！邮箱格式错误！");
            tfMail.clear();
            tfNotice.setVisible(true);
        } else if (!isCorrectMailAndPassword()) {
            tfNotice.setText("登录失败！账号密码不匹配");
            tfPassword.clear();
            tfNotice.setVisible(true);
        } else {// 登录成功
            tfNotice.setText("登录成功！");
            tfNotice.setVisible(true);
            try {
                Socket socket = client.login();
                System.out.println("loginSocket:" + socket);
                application.goMain(tfMail.getText(), socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setApp(ClientUI application) {
        this.application = application;
    }

    public boolean isValidAddress() {

        String regex = "^\\s*\\w+(?:\\.?[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(regex);
        System.out.println(tfMail.getText());
        Matcher m = p.matcher(tfMail.getText());

        return m.matches();
    }

    public boolean isCorrectMailAndPassword() {
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
