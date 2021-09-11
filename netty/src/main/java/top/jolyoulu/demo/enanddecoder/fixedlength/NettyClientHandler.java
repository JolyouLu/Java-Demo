package top.jolyoulu.demo.enanddecoder.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 14:51
 * @Version 1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static String[] alphabets = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P"};

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append("这是第");
            builder.append(i);
            builder.append("条消息，内容是：");
            for (int j = 0; j < 100; j++) {
                builder.append(alphabets[i]);
            }
            builder.append("....");
            ByteBuf buf = Unpooled.copiedBuffer(builder.toString(), StandardCharsets.UTF_8);
            System.out.println("客户端发送数据包长度："+buf.readableBytes());
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("客户端接收到消息："+msg.toString(StandardCharsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
