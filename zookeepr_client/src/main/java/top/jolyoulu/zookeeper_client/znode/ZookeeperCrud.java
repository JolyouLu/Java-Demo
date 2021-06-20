package top.jolyoulu.zookeeper_client.znode;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.NIOServerCnxn;
import top.jolyoulu.zookeeper_client.watcher.ZookeeperWatcher;

import java.io.IOException;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 14:52
 * @Version 1.0
 */
public class ZookeeperCrud {

    private String connectString="192.168.100.101:2181";
    protected ZooKeeper zooKeeper;

    /**
     * 建立连接
     */
    public ZookeeperCrud() {
        try {
            this.zooKeeper = new ZooKeeper(connectString,5000,null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立连接
     */
    public ZookeeperCrud(Watcher watcher) {
        try {
            this.zooKeeper = new ZooKeeper(connectString,5000,watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个持久无序、无权限控制节点
     * @param path 路径
     * @param data 数据
     * @return
     */
    public String createPersistent(String path,String data){
        try {
            return zooKeeper.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一个临时无序、无权限控制节点
     * @param path 路径
     * @param data 数据
     * @return
     */
    public String createEphemeral(String path,String data){
        try {
            return zooKeeper.create(path,data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 更新信息
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        byte data[] = zooKeeper.getData(path,false,null);
        data = (data == null)? "null".getBytes() : data;
        return new String(data);
    }


    /***
     * 更新信息
     * @param path
     * @param data
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat setData(String path, String data) throws KeeperException, InterruptedException {
        return zooKeeper.setData(path, data.getBytes(), -1);
    }

    /***
     * 是否存在
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat exists(String path) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path,false);

    }


    /***
     * 删除
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delete(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path,-1);
    }
    /***
     * 删除 递归
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteRecursive(String path) throws KeeperException, InterruptedException {
        ZKUtil.deleteRecursive(zooKeeper, path);
    }

    /**
     * 隔壁客户端连接
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
