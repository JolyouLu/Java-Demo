import com.sun.jmx.mbeanserver.JmxMBeanServer;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnection;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/7 15:54
 * @Description
 */
public class SimpleJmxClient {
    private static final String JMX_USERNAME = "admin";
    private static final String JMX_PASSWORD = "password";

    public static void main(String[] args) {
        String jmxUrl = "service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi";
        try {
            JMXServiceURL jmxServiceURL = new JMXServiceURL(jmxUrl);
            Map<String, Object> env = new HashMap<>();
            env.put(JMXConnector.CREDENTIALS, new String[] { JMX_USERNAME, JMX_PASSWORD });
            JMXConnector connector = JMXConnectorFactory.newJMXConnector(jmxServiceURL, env);
            connector.connect();
            MBeanServerConnection msc = connector.getMBeanServerConnection();
            System.out.println("远程服务注册的MBean数量："+msc.getMBeanCount());
            System.out.println("==========返回所有MBean当前注册的域列表============");
            Set<ObjectName> queryMBeans = msc.queryNames(null, null);
            for (ObjectName objectName : queryMBeans) {
                System.out.println(objectName);
            }
            System.out.println("==========MBean: com.sun.management============");
            ObjectName objectName = ObjectName.getInstance("JL:type=customer");
            MBeanInfo mBeanInfo = msc.getMBeanInfo(objectName);
            System.out.println("可操作的方法：");
            for (MBeanOperationInfo operation : mBeanInfo.getOperations()) {
                System.out.println(operation);
            }
            ObjectInstance mscObjectInstance = msc.getObjectInstance(objectName);
            System.out.println(mscObjectInstance);
            Object invoke = msc.invoke(objectName, "say", null, null);
            System.out.println(invoke);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
