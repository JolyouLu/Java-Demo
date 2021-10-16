package top.jolyoulu.Interceptor.bean;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/7 17:31
 * @Version 1.0
 *
 */
public class TraceSession {

    static ThreadLocal<TraceSession> session = new ThreadLocal<>();

    private String traceId;
    private String parentId;
    private int currentEventId;

    //开启会话
    public TraceSession(String traceId, String parentId) {
        this.traceId = traceId;
        this.parentId = parentId;
        session.set(this);
    }

    //获取当前事件id
    public int getNextCurrentEventId(){
        return ++currentEventId;
    }

    //获取会话
    public static TraceSession getCurrentSession(){
        return session.get();
    }

    //关闭会话
    public static void close(){
        session.remove();
    }

    public static ThreadLocal<TraceSession> getSession() {
        return session;
    }

    public static void setSession(ThreadLocal<TraceSession> session) {
        TraceSession.session = session;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setCurrentEventId(int currentEventId) {
        this.currentEventId = currentEventId;
    }

    public int getCurrentEventId() {
        return currentEventId;
    }
}
