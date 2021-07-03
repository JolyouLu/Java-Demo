package top.jolyoulu.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/3 14:09
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@ToString
public class User implements Serializable {
    private String id;
    private String username;
    private String log;
}
