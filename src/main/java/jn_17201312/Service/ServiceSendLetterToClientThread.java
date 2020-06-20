package jn_17201312.Service;

import jn_17201312.model.IODeal;
import java.io.*;
import java.net.Socket;

public class ServiceSendLetterToClientThread implements Runnable {

    private Socket client = null;

    public ServiceSendLetterToClientThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // 获取Socket的输入流，用来接收从客户端发送过来的数据
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //获取Socket的输出流，用来向客户端发送数据
            PrintStream out = new PrintStream(client.getOutputStream());
            String user = buf.readLine();
            File[] files = IODeal.getFilesFromFolder("Mails/" + user, ".json");
            sendAllFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getFilesNames(File[] files) {
        String[] filesNames = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            filesNames[i] = files[i].getName();
        }
        return filesNames;
    }

    public void sendAllFiles(File[] files) throws IOException {
        String[] filesNames = getFilesNames(files);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());

        // 发送长度和文件名
        dos.writeInt(filesNames.length);
        dos.flush();
        System.out.println("文件数：" + files.length);

        for (File file : files) {
            System.out.println("文件名：" + file.getName() + " 文件大小：" + file.length() + "B");
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
        }

        System.out.println("开始发送文件...");

        byte[] bytes = new byte[1024];
        byte[] endOfFile = {'\n'};
        int length = 0;
        //发送文件内容
        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            dos.write(endOfFile, 0, endOfFile.length);
        }

        System.out.println("文件发送完毕...");
        client.close();
    }
}
