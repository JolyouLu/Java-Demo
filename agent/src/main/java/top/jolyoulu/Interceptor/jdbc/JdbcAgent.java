package top.jolyoulu.Interceptor.jdbc;

import javassist.*;
import top.jolyoulu.Interceptor.bean.TraceSession;
import top.jolyoulu.Interceptor.servlet.ServerAgent;

import java.io.IOException;
import java.io.Serializable;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.ProtectionDomain;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/17 11:43
 * @Version 1.0
 */
public class JdbcAgent {

    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("jdbc-拦截器");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (!"com/mysql/jdbc/NonRegisteringDriver".equals(className)) {
                    return null;
                }
                return build(loader, className.replaceAll("/", "."));
            }
        });
    }

    public static byte[] build(ClassLoader loader, String className) {
        try {
            ClassPool pool = new ClassPool();
            //让pool获有取到LoaderClass能力
            pool.insertClassPath(new LoaderClassPath(loader));
            CtClass ctClass = pool.get(className);
            //获取service方法
            CtMethod method = ctClass.getDeclaredMethod("connect");
            //复制原方法名称
            CtMethod copyMethod = CtNewMethod.copy(method, ctClass, new ClassMap());
            //修改原方法名称
            method.setName(method.getName() + "$agent");
            copyMethod.setBody("{\n" +
                    "                return top.jolyoulu.Interceptor.jdbc.JdbcAgent.proxyConnection(connect$agent($$));\n" +
                    "            }");
            ctClass.addMethod(copyMethod);
            return ctClass.toBytecode();
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static Connection proxyConnection(Connection conn) {
        return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(),
                new Class[]{Connection.class}, new proxyConnection(conn));
    }

    public static PreparedStatement poxyStatement(PreparedStatement statement, Object stat) {
        return (PreparedStatement) Proxy.newProxyInstance(statement.getClass().getClassLoader(),
                new Class[]{PreparedStatement.class}, new PreparedStatementHandler(statement, stat));
    }

    public static class proxyConnection implements InvocationHandler {

        Connection target; //代理的目标对象

        public proxyConnection(Connection target) {
            this.target = target;
        }

        //动态代理
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //代理的目标方法
            boolean isTargetMethod = "prepareStatement".equalsIgnoreCase(method.getName());
            Object stat = null;
            if (isTargetMethod) {
                //jdbc执行事件的开始
                stat = begin(target, (String) args[0]);
            }
            Object result = method.invoke(target, args);
            if (result instanceof PreparedStatement) {
                return poxyStatement((PreparedStatement) result, stat);
            }
            return result;
        }
    }

    public static class PreparedStatementHandler implements InvocationHandler {
        private final PreparedStatement statement;
        private final Object jdbcStat;

        public PreparedStatementHandler(PreparedStatement statement, Object jdbcStat) {
            this.statement = statement;
            this.jdbcStat = jdbcStat;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            try {
                result = method.invoke(statement, args);
            } catch (Throwable e) {
                throw e;
            } finally {
                if ("close".equals(method.getName())) {
                    end(jdbcStat);
                }
            }
            return result;
        }
    }

    //方法开始前调用
    public static Object begin(Connection connection, String sql) {
        JdbcStatistics jdbcStat = new JdbcStatistics();
        try {
            jdbcStat.jdbcUrl = connection.getMetaData().getURL();
        }catch (Exception e){
            e.printStackTrace();
        }
        jdbcStat.begin = System.currentTimeMillis();
        TraceSession session = TraceSession.getCurrentSession();
        if (session != null){
            jdbcStat.traceId = session.getTraceId();
            jdbcStat.eventId = session.getParentId() + "." + session.getNextCurrentEventId();
        }
        jdbcStat.sql = sql;
        return jdbcStat;
    }

    //方法结束后调用
    public static void end(Object param) {
        System.out.println(param);
    }

    // 实现 jdbc 数据采集器
    public static class JdbcStatistics implements Serializable {
        private String traceId;
        private String eventId;
        private Long useTime;
        public Long begin;// 时间戳
        // jdbc url
        public String jdbcUrl;
        // sql 语句
        public String sql;
        // 数据库名称
        public String databaseName;

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

        public Long getBegin() {
            return begin;
        }

        public void setBegin(Long begin) {
            this.begin = begin;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public Long getUseTime() {
            return useTime;
        }

        public void setUseTime(Long useTime) {
            this.useTime = useTime;
        }

        @Override
        public String toString() {
            return "JdbcStatistics{" +
                    "traceId='" + traceId + '\'' +
                    ", eventId='" + eventId + '\'' +
                    ", useTime='" + useTime + '\'' +
                    ", begin=" + begin +
                    ", jdbcUrl='" + jdbcUrl + '\'' +
                    ", sql='" + sql + '\'' +
                    ", databaseName='" + databaseName + '\'' +
                    '}';
        }
    }
}
