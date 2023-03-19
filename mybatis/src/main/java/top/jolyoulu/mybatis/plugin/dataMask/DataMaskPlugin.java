package top.jolyoulu.mybatis.plugin.dataMask;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 0:29
 * @Description 数据脱敏插件
 * 拦截ResultSetHandler的handleResultSets方法
 */
@Intercepts(
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = Statement.class)
)
public class DataMaskPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> records = (List<Object>) invocation.proceed();
        records.forEach(this::dataMask);
        return records;
    }

    //数据脱敏处理
    private void dataMask(Object source) {
        Class<?> sourceClass = source.getClass();
        //真实数据返回的一个包装类
        MetaObject metaObject = SystemMetaObject.forObject(source);
        Stream.of(sourceClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DataMask.class))
                .forEach( field -> doDataMask(metaObject,field));
    }

    //执行脱敏
    private void doDataMask(MetaObject metaObject, Field field) {
        String name = field.getName();
        Object value = metaObject.getValue(name);
        if (value instanceof String){
            DataMask dataMask = field.getAnnotation(DataMask.class);
            DataMaskStrategy strategy = dataMask.strategy();
            String apply = strategy.getDesensitize().apply((String) value);
            metaObject.setValue(name,apply);
        }
    }
}
