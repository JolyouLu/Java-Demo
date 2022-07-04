package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 19:01
 * @Version 1.0
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从客户端"+ctx.channel().remoteAddress()+"读取到一个Long："+msg);
        System.out.println("MyServerHandler 发送数据");
//        ctx.writeAndFlush(123456L);
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        ctx.fireChannelRead(msg+"相同的消息"+id); //测试多次调用fireChannelRead会怎么样
        ctx.fireChannelRead(msg+"相同的消息"+id);
        ctx.fireChannelRead(msg+"相同的消息"+id);
        ctx.fireChannelRead(msg+"相同的消息"+id);
        ctx.fireChannelRead(msg+"相同的消息"+id);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.getMessage();
        ctx.close();
    }
}
