package top.jolyoulu.zookeeper_client.acl;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import top.jolyoulu.zookeeper_client.watcher.ZookeeperWatcher;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 16:59
 * @Version 1.0
 */
public class ZookeeperAcl {

    private String connectString = "192.168.100.101:2181";
    private ZooKeeper zooKeeper;
    /**
     * 认证类型
     */
    final static String scheme = "digest";
    final static String auth = "jolyoulu:123";//用户名:密码

    /**
     * 创建连接
     * @param acl 是否开启认证
     */
    public ZookeeperAcl(boolean acl) {
        try {
            this.zooKeeper = new ZooKeeper(connectString, 5000, null);
            if (acl){
                zooKeeper.addAuthInfo(scheme, auth.getBytes());//给当前客户端添加认证信息
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //新增方法===================================================================
    /***
     * 创建持久节点
     * @param path
     * @param data
     * @return
     */
    public String createPersistentAcl(String path, String data) {
        try {
            return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    //新增方法===================================================================

    /***
     * 创建持久节点
     * @param path
     * @param data
     * @return
     */
    public String createPersistent(String path, String data) {
        try {
            return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /***
     * 创建临时节点
     * @param path
     * @param data
     * @return
     */
    public String createEphemeral(String path, String data) {
        try {
            return zooKeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
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
        byte data[] = zooKeeper.getData(path, false, null);
        data = (data == null) ? "null".getBytes() : data;
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
        return zooKeeper.exists(path, false);

    }


    /***
     * 删除
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void delete(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, -1);
    }

    /***
     * 删除
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteRecursive(String path) throws KeeperException, InterruptedException {
        ZKUtil.deleteRecursive(zooKeeper, path);
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }


}
