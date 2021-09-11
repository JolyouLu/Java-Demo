package top.jolyoulu.demo.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 15:13
 * @Version 1.0
 */
public class NettyServerInitialize extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个netty 提供的一个httpServerCodec(http编码解码器) => [coder - decode]
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //增加一个自定义的处理器 handler
        pipeline.addLast("MyNettyHttpServerHandler",new NettyHttpServerHandler());
        System.out.println("");
    }

}
