package jn_17201312.Service;

import jn_17201312.model.IODeal;
import jn_17201312.model.Mail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServiceReceiveLetterFromClientThread implements Runnable {

    private Socket client = null;

    public ServiceReceiveLetterFromClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //获取Socket的输出流，用来向客户端发送数据
            PrintStream out = new PrintStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从客户端发送过来的数据
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean flag = true;
            Mail mail = new Mail();
            while (flag) {
                // @TODO 把文件以某种格式存在本地Mails文件夹中

                //接收从客户端发送过来的数据
                String str = buf.readLine();
                System.out.println(str);
                if ("[!@END@!]".equals(str)) {
                    flag = false;
                } else {
                    // 传到Mail里
                    mail.inputData(str);
                }
            }
            out.println("success");

            // 存入本地
            IODeal.write(IODeal.translateToJSONObject(mail));

            System.out.println(client.getPort() + ":end");
            out.close();
            client.close();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
