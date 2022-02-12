package top.jolyoulu.poolusage;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/31 17:40
 * @Version 1.0
 */
@Slf4j
public class test {
    public static void main(String[] args) {
        String str = "as12345678";
        Matcher matcher = Pattern.compile("[A-Za-z]{2}[0-9]{8}").matcher(str);
        while (matcher.find()){
            log.debug(matcher.group());
        }
    }
}
