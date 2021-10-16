package top.jolyoulu.Interceptor;

import top.jolyoulu.Interceptor.dubbo.DubboConsumerAgent;
import top.jolyoulu.Interceptor.servlet.ServerLogAgent;
import top.jolyoulu.Interceptor.servlet.ServletLogAgent;

import java.lang.instrument.Instrumentation;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 17:43
 * @Version 1.0
 */
public class MainAgent {
    public static void premain(String arg, Instrumentation instrumentation){
        ServletLogAgent.premain(arg,instrumentation);
        ServerLogAgent.premain(arg, instrumentation);
        DubboConsumerAgent.premain(arg,instrumentation);
    }
}
