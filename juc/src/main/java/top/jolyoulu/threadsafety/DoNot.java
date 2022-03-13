package top.jolyoulu.threadsafety;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2022/3/5 22:32
 * @Version 1.0
 * 请不要这样做这样的打断程序是没有意义的
 */
@Slf4j
public class DoNot {

    public static void main(String[] args) throws InterruptedException {
        Calculator calculator = new Calculator();
        //定义一个任务，对sum循环加法
        class Task extends Thread{
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    calculator.increment();
                }
            }
        }
        //构建2个线程同时执行
        Task t1 = new Task();
        Task t2 = new Task();
        t1.start();
        t2.start();
        //主线程等待其它线程结束
        t1.join();
        t2.join();
        log.debug("合计={}",calculator.getSum());
    }
}
class Calculator {
    private int sum = 0;

    public void increment(){
        synchronized (this){
            sum++;
        }
    }

    public int getSum(){
        synchronized (this){
            return sum;
        }
    }
}
