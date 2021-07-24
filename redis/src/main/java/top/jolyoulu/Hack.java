package top.jolyoulu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 1:20
 * @Version 1.0
 */
public class Hack {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6380);
            Socket accept = serverSocket.accept();
            byte[] b = new byte[1024];
            accept.getInputStream().read();
            System.out.println(new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
