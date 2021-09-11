package top.jolyoulu.demo.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 23:49
 * @Version 1.0
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * @param ctx 上下文
     * @param evt 上一个handler 传过来的事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            //将evt转型IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;
            String evenType = null;
            switch (event.state()){
                case READER_IDLE:
                    evenType = "读空闲";
                    break;
                case WRITER_IDLE:
                    evenType = "写空闲";
                    break;
                case ALL_IDLE:
                    evenType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时事件--"+evenType);
        }
    }
}
