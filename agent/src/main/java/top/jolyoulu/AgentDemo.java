package top.jolyoulu;

import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.*;
import java.security.ProtectionDomain;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/4 20:27
 * @Version 1.0
 * javaanent jar包编写步骤
 * 1.必须META-INF/MANIFEST.MF中指定Premain-Class设置agent驱动类
 * 2.在启动类需要编写启动方法public static void premain(String args, Instrumentation instrumentation)
 * 3.不可直接运行，只能跳过jvm参数-javaagent:xxx.jar附着其它jvm进程运行
 */
public class AgentDemo {

    public static void premain(String args, Instrumentation instrumentation) {
        //如果已经被转载后的类不会在执行transform方法，需要使用redefineClasses重新装载
        AgentServer agentServer = new AgentServer();
        //拦截所有转载类
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) throws IllegalClassFormatException {
                //拦截top/jolyoulu/AgentServer类的装载
                if ("top/jolyoulu/AgentServer".equals(className)){
                    System.out.println("拦截到AgentServer被装载");
                }
                return new byte[0];
            }
        },true);
        //配合canRetransform=true使用，重新转载指定类，会重新执行addTransformer方法
        try {
            instrumentation.retransformClasses(AgentServer.class);
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }

        //手动指定对某一个类重新定义
//        try {
//            InputStream input = AgentServer.class.getResourceAsStream("AgentServer.class");
//            byte[] bytes = IOUtils.readFully(input,-1,false);
//            instrumentation.redefineClasses(new ClassDefinition(AgentServer.class,bytes));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnmodifiableClassException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }
}
