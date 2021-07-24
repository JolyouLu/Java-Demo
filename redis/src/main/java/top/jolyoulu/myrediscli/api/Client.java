package top.jolyoulu.myrediscli.api;

import top.jolyoulu.myrediscli.connection.Connection;
import top.jolyoulu.myrediscli.protocol.Protocol;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 1:14
 * @Version 1.0
 * 简单实现一个自己的Redis客户端
 */
public class Client {

    private Connection connection;

    public Client(String host,int port) {
        this.connection = new Connection(host,port);
    }

    public String set(final String key,String value){
        connection.sendCommand(Protocol.Command.SET,SafeEncode.encode(key),SafeEncode.encode(value));
        return connection.getStatusReply();
    }

    public String get(final String key){
        connection.sendCommand(Protocol.Command.GET,SafeEncode.encode(key));
        return connection.getStatusReply();
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 6379);
        client.set("test","1234567890");
        System.out.println(client.get("test"));
    }
}
