package top.jolyoulu.jetty;


import com.alibaba.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.jolyoulu.AgentServer;
import top.jolyoulu.service.User;
import top.jolyoulu.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @Author: JolyouLu
 * @Date: 2021/10/16 17:35
 * @Version 1.0
 */
public class WebUserServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        try {
            //初始化dubbo服务
            ClassPathXmlApplicationContext context =
                    new ClassPathXmlApplicationContext("spring-consumer.xml");
            context.start();
            userService = context.getBean(UserService.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        new AgentServer().sayHello("lzj","is cool");
        try {
            User user = userService.getUser(id);
            System.out.println("查询结果 => "+user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
