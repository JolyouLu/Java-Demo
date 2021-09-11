package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 18:57
 * @Version 1.0
 * 自定义解码
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("入站 decode 开始解码");
        //因为long 8个字节
        if (in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }

}
