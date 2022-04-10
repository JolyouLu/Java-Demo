package top.jolyoulu.设计模式.交替运行.parkUnpark;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:53
 * @Version 1.0
 */
public class ParkUnpark {
    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * 打印
     * @param str 打印的内容
     * @param next 打印完毕后需要唤醒的线程
     */
    public void print(String str,Thread next){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(next);
        }
    }
}
