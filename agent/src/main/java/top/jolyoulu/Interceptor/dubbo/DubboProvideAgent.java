package top.jolyoulu.Interceptor.dubbo;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.cluster.support.wrapper.MockClusterInvoker;
import javassist.*;
import top.jolyoulu.Interceptor.bean.TraceSession;

import java.io.Serializable;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/16 15:59
 * @Version 1.0
 */
public class DubboProvideAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("dubbo-provide-拦截器");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //非dubbo ClassLoaderFilter类跳过
                if (!"com/alibaba/dubbo/rpc/filter/ClassLoaderFilter".equals(className)){
                    return null;
                }
                try {
                    return build(loader,className.replaceAll("/","."));
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public static byte[] build(ClassLoader loader,String name) throws Exception {
        ClassPool pool = new ClassPool();
        pool.insertClassPath(new LoaderClassPath(loader));
        CtClass ctClass = pool.get(name);
        //修改指定方法字节码
        CtMethod method = ctClass.getDeclaredMethod("invoke");
        //增加监听代码执行时间
        //1、拷贝原方法得到新方法
        CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
        //2、修改原方法名称
        method.setName(method.getName()+"$agent");
        //2、增加监听代码
        copyMethod.setBody("{\n" +
                "              Object traceInfo = top.jolyoulu.Interceptor.dubbo.DubboProvideAgent.begin($args);\n" +
                "              try {\n" +
                "                  return "+copyMethod.getName()+"$agent($$);\n" +
                "              }finally {\n" +
                "                  top.jolyoulu.Interceptor.dubbo.DubboProvideAgent.end(traceInfo);\n" +
                "              }\n" +
                "          }");
        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }

    //方法开始前调用
    public static Object begin(Object[] args){
        Invoker i = (Invoker) args[0];
        RpcInvocation rpcInvocation = (RpcInvocation) args[1];
        String traceId = rpcInvocation.getAttachment("_traceId");
        String parentId = rpcInvocation.getAttachment("_parentId");
        System.out.println("服务接受 traceI => " + traceId);
        //开启会话
        TraceSession session = new TraceSession(traceId,parentId);
        return new Object();
    }

    //方法结束后调用
    public static void end(Object arg){
        TraceSession.getCurrentSession().close();
    }

}
