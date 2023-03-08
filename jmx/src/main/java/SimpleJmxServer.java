import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.*;
import javax.security.auth.Subject;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Collections;
import java.util.HashMap;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/7 15:50
 * @Description
 */
public class SimpleJmxServer {
    private static final String JMX_USERNAME = "admin";
    private static final String JMX_PASSWORD = "password";

    public static void main(String[] args) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("JL:type=customer");
        server.registerMBean(new Test(),objectName);
        try {
            //注册一个端口，绑定url后用于客户端通过rmi方式连接JMXConnectorServer
            LocateRegistry.createRegistry(9999);
            //URL路径的结尾可以随意指定，但如果需要用Jconsole来进行连接，则必须使用jmxrmi
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");
            //添加认证步骤
            HashMap<String, Object> env = new HashMap<>();
            env.put(JMXConnectorServer.AUTHENTICATOR, new JMXAuthenticator() {
                @Override
                public Subject authenticate(Object credentials) {
                    if (credentials instanceof String[]){
                        String[] creds = (String[]) credentials;
                        if (creds.length == 2 && JMX_USERNAME.equals(creds[0]) && JMX_PASSWORD.equals(creds[1])) {
                            return new Subject(true,
                                    Collections.singleton(new JMXPrincipal(JMX_USERNAME)),
                                    Collections.emptySet(),
                                    Collections.emptySet()
                            );
                        }
                    }
                    throw new SecurityException("Invalid credentials");
                }
            });
            JMXConnectorServer jcs = JMXConnectorServerFactory.newJMXConnectorServer(url, env, server);
            System.out.println("begin rmi start");
            jcs.start();
            System.out.println("rmi start");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(60 * 60 * 1000);
    }
}
