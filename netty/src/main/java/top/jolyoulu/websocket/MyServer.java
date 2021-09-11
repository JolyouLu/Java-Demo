package top.jolyoulu.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 13:00
 * @Version 1.0
 */
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//在bossGroup 增加日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为基于http协议，使用http编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块的方式写的，添加chunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            //因为http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合起来
                            //这就是为什么当浏览器发送大量数据时，就会发多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //对应WebSocket的数据是以，帧(frame)形式传递
                            //可以看到WebSocketFrame 下有6个子类
                            //浏览器请求时 ws://localhost:6666/xxx 表示请求的rui
                            //WebSocketServerProtocolHandler 核心功能将来http协议升级为ws协议(WebSocket)，长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            //自定义的handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
