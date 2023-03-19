package top.jolyoulu.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: JolyouLu
 * @Date: 2021/9/1 13:45
 * @Version 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String sex;
    private Integer age;
}
