package top.jolyoulu.mybatis.plugin.dataMask;

import java.util.function.Function;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 0:35
 * @Description
 */
public enum DataMaskStrategy {
    USERNAME(s -> s.replaceAll("(\\s)\\s(\\s*)","$1*$2")),
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1****$2")),
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2")),
    ADDRESS(s -> s.replaceAll("(\\s{8})\\s{4}(\\s*)\\s{4}","$1****$2****")),
    ;

    private final Function<String,String> desensitize;

    DataMaskStrategy(Function<String, String> desensitize) {
        this.desensitize = desensitize;
    }

    public Function<String, String> getDesensitize() {
        return desensitize;
    }
}
