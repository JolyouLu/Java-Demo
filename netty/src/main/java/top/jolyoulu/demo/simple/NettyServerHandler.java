package top.jolyoulu.demo.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @Author: LZJ
 * @Date: 2020/10/2 19:52
 * @Version 1.0
 * 自定义一个Handler 需要继承netty 规定好的HandlerAdapter(规范)
 * 这时候我们的Handler，才能称为真正的handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据的事件(这里可以读取客户端发送的消息)
    //1.ChannelHandlerContext ctx：上下文对象，含有管道pipline，通道channel，地址
    //2.Object msg：客户端发送的数据 默认是Object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("server ctx ="+ctx);
                //将来msg转成一个 Netty提供的 ByteBuf(不是nio的byteBuffer)
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("客户端发送消息是:"+buf.toString(CharsetUtil.UTF_8));
                System.out.println("客户端地址:"+ctx.channel().remoteAddress());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~",CharsetUtil.UTF_8));
            }
        });
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        //一般我们需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端,已收到你的消息，正在处理~",CharsetUtil.UTF_8));
    }

    //处理异常，发生异常一般关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }



}
