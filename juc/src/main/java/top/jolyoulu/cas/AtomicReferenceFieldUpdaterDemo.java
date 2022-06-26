package top.jolyoulu.cas;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @Author: JolyouLu
 * @Date: 2022/6/26 12:26
 * @Version 1.0
 */
@Slf4j
public class AtomicReferenceFieldUpdaterDemo {
    public static void main(String[] args) {
        Student student = new Student();
        AtomicReferenceFieldUpdater<Student,String> update = AtomicReferenceFieldUpdater.
                newUpdater(Student.class, String.class, "name"); //参数1：对象类型；参数2：字段类型；参数3：字段名称
        System.out.println("更新前："+student);
        while (true){
            String prev = student.getName(); //获取之前属性
            if (update.compareAndSet(student,prev,"张三")){ //比较替换
                break;
            }
        }
        System.out.println("更新后："+student);
    }
}
@Data
@ToString
class Student{
    volatile String name;
}