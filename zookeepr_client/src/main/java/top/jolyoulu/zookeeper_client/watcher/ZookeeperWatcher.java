package top.jolyoulu.zookeeper_client.watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/20 15:49
 * @Version 1.0
 */
public class ZookeeperWatcher implements Watcher {

    private String connectString = "192.168.100.101:2181";
    private ZooKeeper zooKeeper;

    public ZookeeperWatcher() {
        try {
            this.zooKeeper = new ZooKeeper(connectString, 5000, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    //新增方法如下====================================================================================

    /***
     * 更新信息
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path, boolean watcher) throws KeeperException, InterruptedException {
        byte data[] = zooKeeper.getData(path, watcher, null);
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
    public Stat exists(String path, boolean watcher) throws KeeperException, InterruptedException {
        return zooKeeper.exists(path, watcher);

    }

    @Override
    public void process(WatchedEvent event) {
        // 连接状态
        Event.KeeperState keeperState = event.getState();
        // 事件类型
        Event.EventType eventType = event.getType();
        // 受影响的path
        String path = event.getPath();

        System.out.println("连接状态:" + keeperState + ",事件类型：" + eventType + ",受影响的path:" + path);
        System.out.println("--------------------");
        if (Event.EventType.None.equals(eventType)) {

        }

        try {
            if (null != path && null != this.exists(path, true)) {
                System.out.println("内容:" + this.getData(path, true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
