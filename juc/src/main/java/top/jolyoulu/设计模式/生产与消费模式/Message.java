package top.jolyoulu.设计模式.生产与消费模式;

import lombok.*;

/**
 * @Author: JolyouLu
 * @Date: 2022/4/4 21:48
 * @Version 1.0
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public final class Message {
    private Integer id;
    private Object value;
}
