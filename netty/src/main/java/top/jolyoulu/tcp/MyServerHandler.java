package top.jolyoulu.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.UUID;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 16:39
 * @Version 1.0
 */
public class MyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        String message = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("服务端接收到数据"+message);
        System.out.println("服务端接收到消息量"+ (++count));
        //服务器回送数据给客户端，回送随机的id
        ByteBuf id = Unpooled.copiedBuffer(UUID.randomUUID().toString(), CharsetUtil.UTF_8);
        ctx.writeAndFlush(id);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
