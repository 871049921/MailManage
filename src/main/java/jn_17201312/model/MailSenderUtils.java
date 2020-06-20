package jn_17201312.model;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class MailSenderUtils {
    // 发件人地址
    public String senderAddress;
    // 收件人地址
    public String recipientAddress;
    // 发件人账户名
    public String senderAccount;
    // 发件人账户密码
    public String senderPassword;
    // 协议
    Properties props;
    // 对话
    Session session;
    // 邮件格式
    Message msg;
    // 邮件
    Mail mail;

    private MailSenderUtils() {
        props = new Properties();
    }

    public MailSenderUtils(String authenticationMethod, String
            transportProtocol, String serverAddress) {
        props = new Properties();
        props.setProperty("mail." + authenticationMethod + ".auth", "true");
        props.setProperty("mail.transport.protocol", transportProtocol);
        props.setProperty("mail." + authenticationMethod + ".host", serverAddress);
    }

    public boolean send() {
        if (!canSend()) {
            return false;
        }
        session = Session.getInstance(props);
        try {
            msg = getMimeMessage(session);
            // 根据session对象获取邮件传输对象Transport
            Transport transport = session.getTransport();
            transport.connect(senderAccount, senderPassword);
            //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());
            //如果只想发送给指定的人，可以如下写法
            //transport.sendMessage(msg, new Address[]{new InternetAddress("xxx@qq.com")});
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public MimeMessage getMimeMessage(Session session) throws Exception {
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));
        //设置邮件主题
        msg.setSubject(mail.getSubject(), "UTF-8");
        //设置邮件正文
        msg.setContent(mail.getContext(), "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());

        return msg;
    }

    public void setSessionDebug(boolean condition) {
        session.setDebug(condition);
    }

    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public boolean canSend() {
        boolean flag = false;
        if (null != mail && null != senderAccount && null != senderPassword && null != senderAddress && null != recipientAddress) {
            flag = true;
        }
        return flag;
    }
}
