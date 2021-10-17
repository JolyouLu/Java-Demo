package top.jolyoulu.Interceptor;

import top.jolyoulu.Interceptor.dubbo.DubboConsumerAgent;
import top.jolyoulu.Interceptor.dubbo.DubboProvideAgent;
import top.jolyoulu.Interceptor.jdbc.JdbcAgent;
import top.jolyoulu.Interceptor.servlet.ServerAgent;
import top.jolyoulu.Interceptor.servlet.ServletAgent;

import java.lang.instrument.Instrumentation;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 17:43
 * @Version 1.0
 */
public class MainAgent {
    public static void premain(String arg, Instrumentation instrumentation){
        ServletAgent.premain(arg,instrumentation);
        ServerAgent.premain(arg, instrumentation);
        DubboConsumerAgent.premain(arg,instrumentation);
        DubboProvideAgent.premain(arg,instrumentation);
        JdbcAgent.premain(arg,instrumentation);
    }
}
