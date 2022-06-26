package top.jolyoulu.cas;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/26 12:50
 * @Version 1.0
 */
public class AtomicAdderDemo {
    public static void main(String[] args) {
        //使用原子性整数累加，执行10次获取平均测试结果
        System.out.println("==============AtomicLong==============");
        for (int i = 0; i < 10; i++) {
            demo(
                    () -> new AtomicLong(0),
                    (atomicLong) -> atomicLong.getAndIncrement()
            );
        }
        //使用jdk 1.8提供LongAdder累加器 执行10次获取平均测试结果
        System.out.println("==============LongAdder==============");
        for (int i = 0; i < 10; i++) {
            demo(
                    () -> new LongAdder(),
                    (atomicLong) -> atomicLong.increment()
            );
        }

    }
    private static <T> void demo(Supplier<T> addSupplier, Consumer<T> action){
        T adder = addSupplier.get();
        ArrayList<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.currentTimeMillis();
        //启动所有线程
        ts.forEach(Thread::start);
        //join 所以线程等待结束
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        //打印数组内容
        System.out.println("结果:"+adder+" 耗时:"+(end - start)+"ms");
    }
}
