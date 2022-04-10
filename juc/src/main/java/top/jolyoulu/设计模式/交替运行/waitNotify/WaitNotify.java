package top.jolyoulu.设计模式.交替运行.waitNotify;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/10 18:25
 * @Version 1.0
 * 交替打印
 * a     期望flag=1        打印后修改flag=2
 * b     期望flag=2        打印后修改flag=3
 * c     期望flag=3        打印后修改flag=1
 */
public class WaitNotify {

    private static final Object lock = new Object();
    private int flag;
    private int loopNumber; //循环次数

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    /**
     * 打印方法
     * @param str 要打印的支付
     * @param waitFlag 期望flag
     * @param nextFlag 执行打印后，修改flag并唤醒其它线程
     */
    public void print(String str,int waitFlag,int nextFlag){
        for (int i = 0; i < loopNumber; i++) { //循环loopNumber次
            synchronized (lock){ //获得锁
                while (flag != waitFlag){ //如果当前flag与期望的flag不一致，挂起当前线程
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //flag与期望的flag一致，执行业务代码
                System.out.println(str);
                flag = nextFlag; //将修改的值赋flag
                lock.notifyAll(); //唤醒其它等待的线程
            }
        }
    }
}
