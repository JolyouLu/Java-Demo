package top.jolyoulu.demo.protobuf.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 17:24
 * @Version 1.0
 */
public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMessageEncoder encode 被调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
