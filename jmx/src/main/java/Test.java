import javax.management.DynamicMBean;
import javax.management.modelmbean.ModelMBean;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/7 17:11
 * @Description
 */
public class Test implements TestMBean {
    @Override
    public String say() {
        System.out.println("Hello World");
        return "Hello World";
    }
}
