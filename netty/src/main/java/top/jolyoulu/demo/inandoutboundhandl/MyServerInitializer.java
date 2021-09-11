package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 18:55
 * @Version 1.0
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //入栈handler 进行解码 MyByteToLongDecoder
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyServerHandler());
    }

}
