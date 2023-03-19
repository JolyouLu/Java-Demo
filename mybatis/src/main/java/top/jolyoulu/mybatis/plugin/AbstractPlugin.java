package top.jolyoulu.mybatis.plugin;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: JolyouLu
 * @Date: 2023/3/19 17:17
 * @Description
 */
@Slf4j
public abstract class AbstractPlugin {

    //是否是debug模式
    private boolean debug = false;

    public AbstractPlugin() {
    }

    public AbstractPlugin(boolean debug) {
        this.debug = debug;
    }

    protected void log(String msg,Object... arg){
        if (debug){
            log.debug(msg,arg);
        }
    }
}
