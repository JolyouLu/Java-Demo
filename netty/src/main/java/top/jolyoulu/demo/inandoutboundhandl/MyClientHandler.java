package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 19:10
 * @Version 1.0
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从服务端"+ctx.channel().remoteAddress()+"读取到一个Long："+msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);
    }
}
