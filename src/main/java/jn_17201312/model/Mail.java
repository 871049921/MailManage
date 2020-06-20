package jn_17201312.model;

public class Mail {

    private String sender;
    private String receiver;
    private String subject;
    private String time;
    private String context;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean inputData(String str) {
        String Sender = "[!@Sender@!]";
        String Receiver = "[!@Receiver@!]";
        String Subject = "[!@Subject@!]";
        String Time = "[!@Time@!]";
        String Info = "[!@Info@!]";
        try {
            String res = str.split("@!]")[1];
            if (str.startsWith(Sender)) {
                this.setSender(res);
                return true;
            } else if (str.startsWith(Receiver)) {
                this.setReceiver(res);
                return true;
            } else if (str.startsWith(Subject)) {
                this.setSubject(res);
                return true;
            } else if (str.startsWith(Time)) {
                this.setTime(res);
                return true;
            } else if (str.startsWith(Info)) {
                this.setContext(res);
                return true;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            context += "\n" + str;// 正文
        }

        return false;
    }
}
