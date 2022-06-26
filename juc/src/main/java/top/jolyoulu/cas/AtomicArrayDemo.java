package top.jolyoulu.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/26 11:31
 * @Version 1.0
 */
public class AtomicArrayDemo {
    public static void main(String[] args) {
//        //测试 普通数组非线程安全
//        demo(
//                ()->new int[10], //传入数组
//                (array) -> array.length, //获取数组长度的方法
//                (array,index) -> array[index]++, //对每个元素操作的方法
//                (array) -> System.out.println(Arrays.toString(array)) //打印数组的方法
//        );
//        //AtomicIntegerArray 原子性数组 每个元素都是线程安全
//        demo(
//                ()-> new AtomicIntegerArray(10), //传入数组
//                (array) -> array.length(), //获取数组长度的方法
//                (array,index) -> array.getAndIncrement(index), //对每个元素操作的方法
//                (array) -> System.out.println(array) //打印数组的方法
//        );
        //AtomicReferenceArray 原子性引用数组 每个元素都是线程安全
        demo(
                ()-> new AtomicReferenceArray<Counter>(10), //传入数组
                (array) -> array.length(), //获取数组长度的方法
                (array,index) -> {
                    while (true){
                        Counter prev = array.get(index); //获取当前值
                        Counter next; //定义需要更新成什么值
                        if (Objects.isNull(prev)){
                            next = new Counter(1); //如果null初始一个
                        }else {
                            next = new Counter(prev.getNum() + 1); //否则累加
                        }
                        if (array.compareAndSet(index,prev,next)){ //执行CAS
                            break;
                        }
                    }
                }, //对每个元素操作的方法
                (array) -> System.out.println(array) //打印数组的方法
        );
    }

    private static <T> void demo(Supplier<T> arraySupplier, Function<T,Integer> lengthFun,
                                 BiConsumer<T,Integer> putConsumer, Consumer<T> printConsumer){
        ArrayList<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get(); //获取数组
        Integer length = lengthFun.apply(array); //获取数组长度
        for (int i = 0; i < length; i++) { //遍历数组
            ts.add(new Thread(()->{
                for (int j = 0; j < 10000; j++) { //每次遍历都会执行1w次循环，每个循环会
                    putConsumer.accept(array,j%length); //每次循环会对指定元素操作 %j 确保能够平均分配到所有元素上
                }
            }));
        }
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
        //打印数组内容
        printConsumer.accept(array);
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    static class Counter{
        private int num;
    }
}
