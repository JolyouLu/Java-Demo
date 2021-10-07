package top.jolyoulu.Interceptor;

import javassist.*;
import javassist.bytecode.AccessFlag;
import top.jolyoulu.utils.WildcardMatcher;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Queue;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 12:38
 * @Version 1.0
 * 使用Agent实现无代码侵入性的日志记录
 */
public class ServerLogAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("server 拦截");
        //确定采集目标
        //通配符 xxx.xxx.server.*Server
        WildcardMatcher matcher = new WildcardMatcher(args);
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                //如果className或者loader为空，按默认的转载
                if (className == null || loader == null){
                    return null;
                }
                //如果不匹配正则表达式的类不处理
                if (!matcher.matches(className.replaceAll("/","."))) {
                    return null;
                }
                //匹配的做字节码插桩
                return buildMonitorBytes(loader,className.replace("/","."));
            }
        });
    }

    //插桩逻辑
    private static byte[] buildMonitorBytes(ClassLoader loader, String className) {
        try {
            ClassPool pool = new ClassPool();
            //让pool获有取到LoaderClass能力
            pool.insertClassPath(new LoaderClassPath(loader));
            CtClass ctClass = pool.get(className);
            //获取该类所有方法，遍历
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                //过滤非静态，非抽象，非本地(native)方法
                if (!AccessFlag.isPublic(method.getModifiers())){
                    continue;
                }
                if ((method.getModifiers() & AccessFlag.ABSTRACT) != 0) {
                    continue;
                }
                if ((method.getModifiers() & AccessFlag.STATIC) != 0) {
                    continue;
                }
                if ((method.getModifiers() & AccessFlag.NATIVE) != 0) {
                    continue;
                }
                //复制原方法名称
                CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
                //修改原方法名称
                method.setName(method.getName()+"$agent");
                if (copyMethod.getReturnType().getName().equals("void")) {
                    copyMethod.setBody("{\n" +
                            "                    Object traceInfo = top.jolyoulu.Interceptor.ServerLogAgent.begin($args);\n" +
                            "                    try {\n" +
                            "                        "+copyMethod.getName()+"$agent($$);\n" +
                            "                    }finally {\n" +
                            "                        top.jolyoulu.Interceptor.ServerLogAgent.end(traceInfo);\n" +
                            "                    }\n" +
                            "                }");
                }else {
                    copyMethod.setBody("{\n" +
                            "                    Object traceInfo = top.jolyoulu.Interceptor.ServerLogAgent.begin($args);\n" +
                            "                    try {\n" +
                            "                        return "+copyMethod.getName()+"$agent($$);\n" +
                            "                    }finally {\n" +
                            "                        top.jolyoulu.Interceptor.ServerLogAgent.end(traceInfo);\n" +
                            "                    }\n" +
                            "                }");
                }
                ctClass.addMethod(copyMethod);
            }
            return ctClass.toBytecode();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    //方法开始前调用
    public static TraceInfo begin(Object[] args){
        TraceInfo t = new TraceInfo(System.currentTimeMillis(), args);
        if (TraceSession.getCurrentSession() != null){
            t.setTraceId(TraceSession.getCurrentSession().getTraceId());
            t.setEventId(TraceSession.getCurrentSession().getParentId()+"."+TraceSession.getCurrentSession().getNextCurrentEventId());
        }
        return t;
    }

    //方法结束后调用
    public static void end(Object param){
        TraceInfo traceInfo = (TraceInfo) param;
        System.out.println("执行时间："+(System.currentTimeMillis() - traceInfo.getBegin()));
        System.out.println(traceInfo);
    }

    //用于记录一个方法的开始时间与参数参数的类
    public static class TraceInfo{
        private String traceId;
        private String eventId;
        long begin;
        Object[] args;

        public TraceInfo() {
        }

        public TraceInfo(long begin, Object[] args) {
            this.begin = begin;
            this.args = args;
        }

        public long getBegin() {
            return begin;
        }

        public void setBegin(long begin) {
            this.begin = begin;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

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

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("TraceInfo{");
            sb.append("traceId='").append(traceId).append('\'');
            sb.append(", eventId='").append(eventId).append('\'');
            sb.append(", begin=").append(begin);
            sb.append(", args=").append(args == null ? "null" : Arrays.asList(args).toString());
            sb.append('}');
            return sb.toString();
        }
    }

}
