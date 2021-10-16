package top.jolyoulu.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/16 17:01
 * @Version 1.0
 */
public class WebAgentTest {
    public static void main(String[] args) {
        try {
            Server server = new Server(8008);
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setResourceBase(WebAgentTest.class.getResource("/webapp/").getPath());
            context.setDescriptor(WebAgentTest.class.getResource("/webapp/WEB-INF/web.xml").getPath());
            server.setHandler(context);
            server.start();
            System.out.println("jetty容器启动成功");
            server.join();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
