package top.jolyoulu.demo.inandoutboundhandl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 19:05
 * @Version 1.0
 */
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入入/出站的handler 对数据进行编码/解码
        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyByteToLongDecoder());
        //加入一个自定义的handler，处理业务逻辑
        pipeline.addLast(new MyClientHandler());
    }

}
