package top.jolyoulu.设计模式.交替运行.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:36
 * @Version 1.0
 */
public class AwaitSignal extends ReentrantLock {
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * 打印方法
     * @param str 打印的内容
     * @param current 进入那间休息室
     * @param next 打印内容后唤醒那个休息室
     */
    public void print(String str, Condition current,Condition next){
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await(); //先让当前线程进入相应的休息室
                System.out.println(str); //如果被唤醒后执行如下代码
                next.signal(); //唤醒指定休息室的线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}
