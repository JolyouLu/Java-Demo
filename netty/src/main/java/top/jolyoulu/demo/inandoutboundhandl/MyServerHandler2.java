package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 19:01
 * @Version 1.0
 */
public class MyServerHandler2 extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("MyServerHandler2 收到消息"+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.getMessage();
        ctx.close();
    }
}
