package top.jolyoulu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 15:46
 * @Version 1.0
 * 将百度网页下载到数组中
 */
public class Downloader {
    public static List<String> download() throws IOException{
        URLConnection conn = new URL("https://www.baidu.com/").openConnection();
        ArrayList<String> res = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null){
                res.add(line);
            }
        }
        return res;
    }
}
