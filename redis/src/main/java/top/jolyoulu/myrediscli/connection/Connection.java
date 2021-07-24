package top.jolyoulu.myrediscli.connection;

import top.jolyoulu.myrediscli.protocol.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 1:10
 * @Version 1.0
 */
public class Connection {
    private Socket socket;
    private String host;
    private int port;
    private OutputStream outputStream;
    private InputStream inputStream;

    public Connection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //向Redis发送指令
    public Connection sendCommand(Protocol.Command command,byte[] ...bytes){
        connect();
        Protocol.sendCommand(outputStream,command,bytes);
        return this;
    }

    //建立连接，并获取流
    public void connect(){
        try {
            this.socket = new Socket(host,port);
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getStatusReply() {
        byte[] bytes = new byte[1024];
        try {
            socket.getInputStream().read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }
}
