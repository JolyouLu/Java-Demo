package top.jolyoulu.Interceptor.servlet;

import javassist.*;
import top.jolyoulu.Interceptor.bean.TraceSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 14:02
 * @Version 1.0
 * 使用Agent实现Servlet入口拦截
 */
public class ServletLogAgent {

    //拦截javax.servlet.http.HttpServlet.service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    public static void premain(String arg, Instrumentation instrumentation){
        System.out.println("servlet 拦截");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"javax/servlet/http/HttpServlet".equals(className)){
                    return null;
                }
                return build(loader,className.replaceAll("/","."));
            }
        });
    }

    private static byte[] build(ClassLoader loader, String className) {
        try {
            ClassPool pool = new ClassPool();
            //让pool获有取到LoaderClass能力
            pool.insertClassPath(new LoaderClassPath(loader));
            CtClass ctClass = pool.get(className);
            //获取service方法
            CtMethod method = ctClass.getDeclaredMethod("service",
                    pool.get(new String[]{"javax.servlet.http.HttpServletRequest", "javax.servlet.http.HttpServletResponse"}));
            //复制原方法名称
            CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
            //修改原方法名称
            method.setName(method.getName()+"$agent");
            copyMethod.setBody("{\n" +
                    "                    Object trace = top.jolyoulu.Interceptor.servlet.ServletLogAgent.begin($args);\n" +
                    "                    try {\n" +
                    "                        "+copyMethod.getName()+"$agent($$);\n" +
                    "                    }finally {\n" +
                    "                        top.jolyoulu.Interceptor.servlet.ServletLogAgent.end(trace);\n" +
                    "                    }\n" +
                    "                }");
            ctClass.addMethod(copyMethod);
            return ctClass.toBytecode();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    //方法开始前调用
    public static WebTraceInfo begin(Object[] args){
        HttpServletRequest request = (HttpServletRequest)args[0];
        HttpServletResponse response = (HttpServletResponse) args[1];
        //记录参数
        WebTraceInfo trace = new WebTraceInfo();
        trace.setParams(request.getParameterMap());
        trace.setCookies(request.getCookies());
        trace.setUrl(request.getRequestURI());
        trace.setBegin(System.currentTimeMillis());

        String traceId = UUID.randomUUID().toString().replaceAll("-","");
        TraceSession session = new TraceSession(traceId, "0");
        trace.setTraceId(traceId);
        trace.setEventId(session.getParentId()+"."+session.getNextCurrentEventId());

        return trace;
    }

    //方法结束后调用
    public static void end(Object param){
        WebTraceInfo trace = (WebTraceInfo) param;
        System.out.println("执行时间："+(System.currentTimeMillis() - trace.getBegin()));
        System.out.println(trace);
        TraceSession.getCurrentSession().close();
    }

    //保存采集的信息
    public static class WebTraceInfo{
        private String traceId;
        private String eventId;
        //begin
        private Long begin;
        //url
        private String url;
        //param
        private Map<String,String[]> params;
        //cookie
        private Cookie[] cookies;
        //head
        private String handler;
        private Long userTime;

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

        public Map<String, String[]> getParams() {
            return params;
        }

        public void setParams(Map<String, String[]> params) {
            this.params = params;
        }

        public Cookie[] getCookies() {
            return cookies;
        }

        public void setCookies(Cookie[] cookies) {
            this.cookies = cookies;
        }

        public String getHandler() {
            return handler;
        }

        public void setHandler(String handler) {
            this.handler = handler;
        }

        public Long getUserTime() {
            return userTime;
        }

        public void setUserTime(Long userTime) {
            this.userTime = userTime;
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
            final StringBuffer sb = new StringBuffer("WebTraceInfo{");
            sb.append("traceId='").append(traceId).append('\'');
            sb.append(", eventId='").append(eventId).append('\'');
            sb.append(", begin=").append(begin);
            sb.append(", url='").append(url).append('\'');
            sb.append(", params=").append(params);
            sb.append(", cookies=").append(cookies == null ? "null" : Arrays.asList(cookies).toString());
            sb.append(", handler='").append(handler).append('\'');
            sb.append(", userTime=").append(userTime);
            sb.append('}');
            return sb.toString();
        }
    }
}
