package jn_17201312.Client;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Client {

    private final String IP = "127.0.0.1";
    private Socket socket;

    public String sendLetter(String address, String receiver, String subject, String context, ClientUI application) throws IOException {
        //客户端请求与本机在20006端口建立TCP连接
        Socket client = new Socket(IP, 20006);
        client.setSoTimeout(10000);
        //获取Socket的输出流，用来发送数据到服务端
        PrintStream out = new PrintStream(client.getOutputStream());
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //发送数据到服务端
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        out.println("[!@Sender@!]" + address);
        out.println("[!@Receiver@!]" + receiver);
        out.println("[!@Subject@!]" + subject);
        out.println("[!@Time@!]" + simpleDateFormat.format(new Date()));
        out.println("[!@Info@!]" + context);
        out.println("[!@END@!]");

        try {
            String receive = null;
            receive = buf.readLine();
            System.out.println(receive);
            if ("success".equals(receive)) {
                application.alertSendMessage("邮件发送成功");
            } else {
                application.alertSendMessage("邮件发送失败");
            }
            return receive;
        } catch (SocketTimeoutException e) {
            application.alertSendMessage("邮件发送超时");
            return null;
        } finally {
            if (client != null) {
                //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
                client.close(); //只关闭socket，其关联的输入输出流也会被关闭
            }
        }

    }

    // 从服务器读取文件
    public void loadMailFromService(String address) throws IOException {
        Socket client = new Socket(IP, 20007);
        client.setSoTimeout(10000);
        //获取Socket的输出流，用来向服务器发送数据
        PrintStream out = new PrintStream(client.getOutputStream());
        out.println(address);

        InputStream in = client.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        int fileNumber = dis.readInt();
        System.out.println("应接收文件个数：" + fileNumber);
        String[] filesNames = new String[fileNumber];
        long[] fileSize = new long[fileNumber];

        for (int i = 0; i < fileNumber; i++) {
            filesNames[i] = dis.readUTF();
            fileSize[i] = dis.readLong();
            System.out.println("文件名：" + filesNames[i] + "文件大小：" + fileSize[i] + "B");
        }

        System.out.println("開始接收文件");
        // 创建用户目录
        createRootAndUser("Users", address);

        DataInputStream inputStream = new DataInputStream(client.getInputStream());//创建输入流对象
        for (int i = 0; i < fileNumber; i++) {
            try {
                File copyFile = new File("Users/" + address + "/" + filesNames[i]);

                FileOutputStream fos = new FileOutputStream(copyFile); //创建输出流对象
                byte[] data = new byte[1];//创建搬运工具
                int len = 0;//创建长度
                long sum = 0;
                while ((len = inputStream.read(data)) != -1 && sum < fileSize[i]) {//循环读取数据
                    fos.write(data, 0, len);
                    sum += len;
                }
                fos.close();//释放资源
            } catch (FileNotFoundException e) {

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        inputStream.close();//释放资源

        System.out.println("文件接收结束");
    }

    // 判断用户目录是否存在，若不存在则创建
    public void createRootAndUser(String rootURL, String userURL) {
        File root = new File(rootURL);
        File user = new File(rootURL + "/" + userURL);
        if (!root.isDirectory() || !root.exists()) {
            System.out.println("rootMade");
            root.mkdir();
        }
        if (!user.isDirectory() || !user.exists()) {
            System.out.println("userMade");
            user.mkdir();
        } else {
            File[] files = new File(rootURL + "/" + userURL).listFiles();
            for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                if (files[i].exists()) {
                    files[i].delete();
                }
            }
        }
    }


    // 登录并返回Socket
    public Socket login() throws IOException {
        return new Socket(IP, 20005);
    }

    // 登出
    public boolean logout(Socket client) throws IOException {
        PrintStream printStream = new PrintStream(client.getOutputStream());
        printStream.println("end");
        System.out.println(client + "shutdown");
        client.close();
        System.out.println(client.isClosed());
        return client.isClosed() || client.isConnected();
    }
}
