package top.jolyoulu.zookeepr_zkclient.watcher;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.Watcher;
import top.jolyoulu.zookeepr_zkclient.entity.User;
import top.jolyoulu.zookeepr_zkclient.znode.ZkClientCrud;

import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 19:26
 * @Version 1.0
 */
public class ZkClientWatcher {
    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("192.168.100.101:2181",500,500,new MySerializable());
        zkClient.createPersistent("/user",new User("name","123"));
        System.out.println(zkClient.readData("/user").toString());

        //监听/user节点的事件
        zkClient.subscribeDataChanges("/user", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("user节点发生变化！");
            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("user节点被删除了！");
            }
        });

        //监听/user子节点的事件
        zkClient.subscribeChildChanges("/user", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("user节点的子节点发生变化！");
            }
        });

        //监听连接事件
        zkClient.subscribeStateChanges(new IZkStateListener() {
            @Override
            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                switch (state){
                    case SyncConnected:
                        System.out.println("连接成功");
                        break;
                    case Disconnected:
                        System.out.println("连接断开");
                        break;
                    default:
                        System.out.println("其它状态"+state);
                        break;
                }
            }

            @Override
            public void handleNewSession() throws Exception {
                System.out.println("真正重建连接中！");
            }

            @Override
            public void handleSessionEstablishmentError(Throwable error) throws Exception {
                System.out.println("连接建立异常！");
            }
        });
        Thread.sleep(2000);
        //修改/user节点
        zkClient.writeData("/user",new User("test1", "test1"));
        Thread.sleep(1000);
        //给/user节点添加子节点
        zkClient.createPersistent("/user/user1",new User("user1",""));
        Thread.sleep(1000);
        //修改/user子节点
        zkClient.writeData("/user/user1",new User("user2",""));
        Thread.sleep(1000);
        //删除/user子节点
        zkClient.delete("/user/user1");
        Thread.sleep(1000);
        //删除/user节点
        zkClient.delete("/user");
        Thread.sleep(1000);

    }
}
