package top.jolyoulu.demo.protobuf.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 17:24
 * @Version 1.0
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decoder 被调用");
        //将得到的二进制字节码转成指定对象
        int length = in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        //封装成对象
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
