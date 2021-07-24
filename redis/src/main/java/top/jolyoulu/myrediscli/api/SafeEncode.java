package top.jolyoulu.myrediscli.api;

import java.nio.charset.StandardCharsets;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 1:38
 * @Version 1.0
 */
public class SafeEncode {
    public static byte[] encode(String key){
        return key.getBytes(StandardCharsets.UTF_8);
    }
}
