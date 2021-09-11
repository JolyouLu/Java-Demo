package top.jolyoulu.demo.protobuf.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 16:37
 * @Version 1.0
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入编码器
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
