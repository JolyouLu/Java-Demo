package top.jolyoulu.设计模式.保护性暂停.解耦增强;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 17:22
 * @Version 1.0
 */
public class Futures {

    private static Map<String, GuardedObject> futures = new ConcurrentHashMap<>();

    //构建一个GuardedObject
    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        futures.put(go.getId(), go);
        return go;
    }

    //根据id获取
    public static GuardedObject getGuardedObject(String id) {
        return futures.remove(id);
    }

    //获取ids
    public static Set<String> getIds() {
        return futures.keySet();
    }

    //产生唯一id
    private static String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
