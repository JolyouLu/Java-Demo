package top.jolyoulu.cas;

import lombok.Data;
import lombok.ToString;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/26 17:18
 * @Version 1.0
 */
public class UnsafeTest {
    public static void main(String[] args) throws Exception {
        //使用反射获取 Unsafe
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        System.out.println(unsafe);
        //Unsafe对象中的CAS方法
        //1.获取需要替换的属性偏移量
        Teacher teacher = new Teacher();
        long idOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));
        //2.执行cas
        unsafe.compareAndSwapInt(teacher,idOffset,0,1);
        unsafe.compareAndSwapObject(teacher,nameOffset,null,"张三");
        System.out.println(teacher);
    }
}
@Data
@ToString
class Teacher{
    volatile int id;
    volatile String name;
}
