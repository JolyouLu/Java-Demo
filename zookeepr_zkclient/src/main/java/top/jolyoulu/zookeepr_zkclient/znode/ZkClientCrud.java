package top.jolyoulu.zookeepr_zkclient.znode;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import top.jolyoulu.zookeepr_zkclient.entity.User;

import java.io.Serializable;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 18:40
 * @Version 1.0
 */
public class ZkClientCrud {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("192.168.100.101:2181",500,500,new SerializableSerializer());
        zkClient.createPersistent("/user",new User("name","123"));
        System.out.println(zkClient.readData("/user").toString());
    }
}
