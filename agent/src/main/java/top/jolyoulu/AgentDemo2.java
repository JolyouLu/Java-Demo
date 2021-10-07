package top.jolyoulu;

import javassist.*;

import java.io.IOException;
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
public class AgentDemo2 {

    public static void premain(String args, Instrumentation instrumentation){
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //拦截top/jolyoulu/AgentServer类的装载
                if ("top/jolyoulu/AgentServer".equals(className)){
                    return hourMeter();
                }
                return new byte[0];
            }
        },true);

    }

    private static byte[] hourMeter(){
        try {
            ClassPool pool = new ClassPool();
            pool.appendSystemPath();
            CtClass ctClass = pool.get("top.jolyoulu.AgentServer");
            //修改指定方法字节码
            CtMethod ctMethod = ctClass.getDeclaredMethod("sayHello");
            //增加监听代码执行时间
            //1、拷贝原方法得到新方法
            CtMethod copyMethod = CtNewMethod.copy(ctMethod, ctClass, new ClassMap());
            //2、修改原方法名称
            ctMethod.setName("sayHello$agent");
            //2、增加监听代码
            copyMethod.setBody("{\n" +
                    "                long begin = System.currentTimeMillis();\n" +
                    "                try {\n" +
                    "                    return sayHello$agent($$);\n" +
                    "                }finally {\n" +
                    "                    long end = System.currentTimeMillis();\n" +
                    "                    System.out.println(\"方法耗时：\"+(end-begin)+\"毫秒\");\n" +
                    "                }\n" +
                    "            }");
            ctClass.addMethod(copyMethod);
            return ctClass.toBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
