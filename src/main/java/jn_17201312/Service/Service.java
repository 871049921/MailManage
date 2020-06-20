package jn_17201312.Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Service {

    private static final Object lock = new Object();
    public static int number = 0;

    public static void main(String[] args) throws Exception {
        getLetter();
        sendLetter();
        login();
        sumNumber();
    }

    public static synchronized void sumNumber() {
        new Thread(() -> {
            int temp = -1;
            while (true) {
                if (number != temp) {
                    temp = number;
                    System.out.println("当前在线总人数：" + number);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void dealLoginSocket(Socket socket) {
        new Thread(() -> {
            while (true) {
                if (socket.isClosed()) {
                    synchronized (lock) {
                        number --;
                    }
                    break;
                }
            }
        }).start();
    }

    public static void login() throws Exception {
        // 登录监控
        ServerSocket loginServer = new ServerSocket(20005);
        new Thread(() -> {
            while (true) {
                //服务端在20005端口监听客户端请求的TCP监控用户登录
                try {
                    Socket socket = null;
                    socket = loginServer.accept();
                    synchronized (lock) {
                        number ++;
                    }
                    dealLoginSocket(socket);
                    new Thread(new ServiceLogin(socket)).start();// @TODO
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void getLetter() throws Exception {
        ServerSocket sendLetterServer = new ServerSocket(20006);
        new Thread(() -> {
            while (true) {
               //服务端在20006端口监听客户端请求的TCP接收客户端发送的邮件
                try {
                    Socket socket = null;
                    socket = sendLetterServer.accept();
                    new Thread(new ServiceReceiveLetterFromClientThread(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void sendLetter() throws Exception {
        ServerSocket receiveLetterServer = new ServerSocket(20007);;
        new Thread(() -> {
            while (true) {
                //服务端在20007端口监听客户端请求的TCP向客户端发送邮件
                try {
                    Socket socket = null;
                    socket = receiveLetterServer.accept();
                    new Thread(new ServiceSendLetterToClientThread(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
