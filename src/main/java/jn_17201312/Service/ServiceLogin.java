package jn_17201312.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServiceLogin implements Runnable {

    private Socket client = null;

    public ServiceLogin(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println("connected!");
        System.out.println(client);
        while (true) {
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String symbol = bf.readLine();
                // 已退出
                if (symbol.equals("end")) {
                    break;
                }
            } catch (IOException e) {
                break;
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
