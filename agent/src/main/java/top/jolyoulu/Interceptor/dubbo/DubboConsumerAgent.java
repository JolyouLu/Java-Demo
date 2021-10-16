package top.jolyoulu.Interceptor.dubbo;

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
public class DubboConsumerAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("dubbo 拦截");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //非dubbo MockClusterInvoker类跳过
                if (!"com/alibaba/dubbo/rpc/cluster/support/wrapper/MockClusterInvoker".equals(className)){
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
                "              Object traceInfo = top.jolyoulu.Interceptor.dubbo.DubboConsumerAgent.begin($args,$0);\n" +
                "              try {\n" +
                "                  return "+copyMethod.getName()+"$agent($$);\n" +
                "              }finally {\n" +
                "                  top.jolyoulu.Interceptor.dubbo.DubboConsumerAgent.end(traceInfo);\n" +
                "              }\n" +
                "          }");
        ctClass.addMethod(copyMethod);
        return ctClass.toBytecode();
    }

    //方法开始前调用
    public static Object begin(Object[] args,Object invoker){
        RpcInvocation invocation = (RpcInvocation) args[0];
        MockClusterInvoker mockClusterInvoker = (MockClusterInvoker) invoker;
        DubboInfo dubboInfo = new DubboInfo();
        dubboInfo.begin = System.currentTimeMillis();
        dubboInfo.params = invocation.getAttachments();
        dubboInfo.methodName = invocation.getMethodName();
        dubboInfo.interfaceName = mockClusterInvoker.getInterface().getName();
        dubboInfo.url = mockClusterInvoker.getUrl().toFullString();

        TraceSession session = TraceSession.getCurrentSession();
        if (session != null) {
            dubboInfo.traceId = session.getTraceId();
            dubboInfo.eventId = session.getParentId() + "." + session.getNextCurrentEventId();
        }
        return dubboInfo;
    }

    //方法结束后调用
    public static void end(Object dubboInfo){
        System.out.println(dubboInfo);
    }

    //保存调用信息的实体类
    public static class DubboInfo implements Serializable{
        private String traceId;
        private String eventId;
        private String interfaceName;
        private String methodName;
        private Long begin;
        private String url;
        private Map<String,String> params;

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public void setInterfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Long getBegin() {
            return begin;
        }

        public void setBegin(Long begin) {
            this.begin = begin;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            this.params = params;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("DubboInfo{");
            sb.append("traceId='").append(traceId).append('\'');
            sb.append(", eventId='").append(eventId).append('\'');
            sb.append(", interfaceName='").append(interfaceName).append('\'');
            sb.append(", methodName='").append(methodName).append('\'');
            sb.append(", begin=").append(begin);
            sb.append(", url='").append(url).append('\'');
            sb.append(", params=").append(params);
            sb.append('}');
            return sb.toString();
        }
    }
}
