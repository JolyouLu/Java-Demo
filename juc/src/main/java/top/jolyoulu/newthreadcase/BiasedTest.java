package top.jolyoulu.newthreadcase;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/12 12:39
 * @Version 1.0
 */
@Slf4j
public class BiasedTest {
    //偏向锁批量撤销
    static Thread t1,t2,t3;

    public static void main(String[] args) throws Exception {
        test3();
    }

    //增加jvm参数 -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
    //偏向锁演示
    public static void test(){
        MyLock lock = new MyLock();
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
        synchronized (lock){
            log.debug(ClassLayout.parseInstance(lock).toPrintable());
        }
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
    }

    //hashCode会时偏向锁失效
    public static void test1(){
        MyLock lock = new MyLock();
        lock.hashCode(); //调用该方法会禁用掉偏向锁
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
        synchronized (lock){
            log.debug(ClassLayout.parseInstance(lock).toPrintable());
        }
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
    }

    //偏向锁批量重偏向
    public static void test2(){
        Vector<MyLock> list = new Vector<>();
        Thread t1 = new Thread(()->{
            //循环生成30个锁对象，加入list中
            for (int i = 0; i < 30; i++) {
                MyLock lock = new MyLock();
                list.add(lock);
                //加锁：使得这些对象锁偏向t1
                synchronized (lock){
                    log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                }
            }
            //唤醒t2
            synchronized (list){
                list.notifyAll();
            }
        },"t1");
        t1.start();

        Thread t2 = new Thread(()->{
            //阻塞等待t1唤醒
            synchronized (list){
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("=======================================================================");
            //从list获取锁对象
            for (int i = 0; i < 30; i++) {
                MyLock lock = list.get(i);
                list.add(lock);
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                //对这些对象加锁，前20次偏向锁会撤销，升级成轻量锁
                //撤销次数多了后(默认20)，剩下的对象都会批量重偏向到t2
                synchronized (lock){
                    log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                }
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
            }
        },"t2");
        t2.start();
    }

    public static void test3() throws InterruptedException {
        Vector<MyLock> list = new Vector<>();
        t1 = new Thread(()->{
            //循环生成30个锁对象，加入list中
            for (int i = 0; i < 39; i++) {
                MyLock lock = new MyLock();
                list.add(lock);
                //加锁：使得这些对象锁偏向t1
                synchronized (lock){
                    log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                }
            }
            //唤醒t2
            LockSupport.unpark(t2);
        },"t1");
        t1.start();

        t2 = new Thread(()->{
            //阻塞等待t1唤醒
            LockSupport.park();
            log.debug("=======================================================================");
            //从list获取锁对象
            for (int i = 0; i < 39; i++) {
                MyLock lock = list.get(i);
                list.add(lock);
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                //对这些对象加锁，前20次偏向锁会撤销，升级成轻量锁
                //撤销次数多了后(默认20)，剩下的对象都会批量重偏向到t2
                synchronized (lock){
                    log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                }
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
            }
            //唤醒t3
            LockSupport.unpark(t3);
        },"t2");
        t2.start();

        t3 = new Thread(()->{
            //阻塞等待t2唤醒
            LockSupport.park();
            log.debug("=======================================================================");
            //从list获取锁对象
            for (int i = 0; i < 39; i++) {
                MyLock lock = list.get(i);
                list.add(lock);
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                //对这些对象加锁，前20次偏向锁会撤销，升级成轻量锁
                //撤销次数多了后(默认20)，剩下的对象都会批量重偏向到t3
                synchronized (lock){
                    log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
                }
                log.debug(i+"\t"+ClassLayout.parseInstance(lock).toPrintable());
            }
        },"t2");
        t3.start();
        t3.join();
        //新创建的对象也不会有偏向锁了
        log.debug("main \t"+ClassLayout.parseInstance(new MyLock()).toPrintable());
    }
}
class MyLock{}
