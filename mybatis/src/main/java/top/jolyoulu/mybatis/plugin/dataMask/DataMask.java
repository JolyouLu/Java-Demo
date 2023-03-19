package top.jolyoulu.mybatis.plugin.dataMask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 0:34
 * @Description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataMask {

    DataMaskStrategy strategy() ;

}
