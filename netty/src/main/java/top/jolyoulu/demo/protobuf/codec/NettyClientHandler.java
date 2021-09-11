package top.jolyoulu.demo.protobuf.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author: LZJ
 * @Date: 2020/10/2 20:18
 * @Version 1.0
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪时就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端 "+ctx);
        //发送一个Student对象到服务端
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("学生").build();
        ctx.writeAndFlush(student);
    }

    //当通道有读取事件时触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复消息: "+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址: "+ctx.channel().remoteAddress());
    }

    //发送异常时触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
