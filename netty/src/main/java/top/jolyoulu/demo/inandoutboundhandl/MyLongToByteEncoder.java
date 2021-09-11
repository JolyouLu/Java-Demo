package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 19:07
 * @Version 1.0
 * 自定义编码
 */
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("出站 encode 开始编码");
        out.writeLong(msg);
    }
}
