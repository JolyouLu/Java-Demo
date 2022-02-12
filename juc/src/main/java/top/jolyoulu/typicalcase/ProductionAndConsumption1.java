package top.jolyoulu.typicalcase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/31 10:00
 * @Version 1.0
 * 生产者消费者的实例demo
 */
public class ProductionAndConsumption1 {
    //咖啡厅
    private static class Cafe{
        ExecutorService exc = Executors.newCachedThreadPool();
        Queue<Coffee> storage = new LinkedList<>();
        Producer producer = new Producer(this);
        Consumer consumer = new Consumer(this);

        public Cafe() {
            exc.execute(producer);
            exc.execute(consumer);
        }
    }

    //咖啡
    private static class Coffee{
        private final int itemNum;
        public Coffee(int itemNum) { this.itemNum = itemNum; }
        public String toString() { return "Coffee " + itemNum; }
    }

    //生产者
    private static class Producer implements Runnable{
        //计数器，控制生产者总计生产达到某个值后停止
        private int count = 0;
        private Cafe cafe;

        public Producer(Cafe cafe) {
            this.cafe = cafe;
        }

        public int getCount() {
            return count;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()){
                try {
                    //计数器未达到100时持续生产
                    while (count < 100){
                        //制作咖啡
                        Coffee coffee = new Coffee(++count);
                        //将咖啡加入到队列
                        if (cafe.storage.offer(coffee)){
                            System.out.println("Produced " + coffee);
                            //通知消费者消费
                            synchronized (cafe.consumer){
                                cafe.consumer.notifyAll();
                            }
                        }
                        //当队列中咖啡达到10杯，挂起等待消费者通知
                        synchronized (this){
                            while (!(cafe.storage.size() < 10))
                                wait();
                        }
                    }
                    //计数器达到100停止生产并且终止线程池
                    System.out.println("Produced " + count + " Items\nStopping production");
                    cafe.exc.shutdownNow();
                }catch (InterruptedException ie){
                    System.out.println("Producer interrupted");
                    System.out.println("Produced " + count + " Items");
                }
            }
        }
    }

    //消费者
    private static class Consumer implements Runnable{
        //计数器，统计消费总数
        private int consumed = 0;
        //咖啡打包到箱子里面
        private List<Coffee> box = new ArrayList<>();
        private Cafe cafe;

        public Consumer(Cafe cafe) {
            this.cafe = cafe;
        }
        @Override
        public void run() {
            try {
                while (!Thread.interrupted()){
                    synchronized(this) {
                        //当前打包好的咖啡数量，大于生产生产的计数器，消费者挂起
                        synchronized (this){
                            while(!(box.size() < cafe.producer.getCount())) {
                                wait();
                            }
                        }
                        //从队列获取咖啡，打包装箱
                        if(box.add(cafe.storage.poll())) {
                            System.out.println("Consuming Item " + ++consumed);
                            //通知生产者可以生产了
                            synchronized(cafe.producer) {
                                cafe.producer.notifyAll();
                            }
                        }
                    }
                }
            }catch (InterruptedException ie){
                System.out.println("Consumer interrupted");
                System.out.println("Consumed " + consumed + " Items");
            }
        }
    }

    public static void main(String[] args) {
        new Cafe();
    }
}
