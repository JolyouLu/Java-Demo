package top.jolyoulu.demo.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 23:32
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
                            //加入一个netty 提供的IdleStateHandler
                            //IdleStateHandler 是netty 提供的处理空闲状态的处理器
                            //long readerIdleTime : 多长时间Server没读事件发生，就会发送一个心跳检测包，检测是否还是连接
                            //long writerIdleTime : 多长时间Server没写事件发生，就会发送一个心跳检测包，检测是否还是连接
                            //long allIdleTime : 多长时间Server没读写事件发生，就会发送一个心跳检测包，检测是否还是连接
                            //当IdleStateHandler触发后，会传递给管道的下一个handler处理，通过handler的userEventTiggered方法处理
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
